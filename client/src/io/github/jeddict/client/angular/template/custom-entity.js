function generateEntityQueries(relationships, entityInstance, dto) {
    var queries = [];
    var variables = [];
    var hasManyToMany = false;
    relationships.forEach(function (relationship) {
        var query;
        var variableName;
        hasManyToMany = hasManyToMany || relationship.relationshipType === 'many-to-many';
        if (relationship.relationshipType === 'one-to-one' && relationship.ownerSide === true && relationship.otherEntityName !== 'user') {
            variableName = relationship.relationshipFieldNamePlural.toLowerCase();
            if (variableName === entityInstance) {
                variableName += 'Collection';
            }
            var relationshipFieldName = "this." + entityInstance + "." + relationship.relationshipFieldName;
            var relationshipFieldNameIdCheck = dto === 'no' ?
                    "!" + relationshipFieldName + " || !" + relationshipFieldName + ".id" :
                    "!" + relationshipFieldName + "Id";

            query = 
                    "this." + relationship.otherEntityName + "Service\n" +
                    "            .query({filter: '" + relationship.otherEntityRelationshipName.toLowerCase() + "-is-null'})\n" +
                    "            .subscribe((res: HttpResponse<"+relationship.otherEntityAngularName+"[]>) => {\n" +
                    "                if (" + relationshipFieldNameIdCheck + ") {\n" +
                    "                    this." + variableName + " = res.body;\n" +
                    "                } else {\n" +
                    "                    this." + relationship.otherEntityName + "Service\n" +
                    "                        .find(" + relationshipFieldName + (dto === 'no' ? '.id' : 'Id') + ")\n" +
                    "                        .subscribe((subRes: HttpResponse<" + relationship.otherEntityAngularName + ">) => {\n" +
                    "                            this." + variableName + " = [subRes.body].concat(res.body);\n" +
                    "                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));\n" +
                    "                }\n" +
                    "            }, (res: HttpErrorResponse) => this.onError(res.message));";
        } else if (relationship.relationshipType !== 'one-to-many') {
            variableName = relationship.otherEntityNameCapitalizedPlural.toLowerCase();
            if (variableName === entityInstance) {
                variableName += 'Collection';
            }
            query = 
                    "this." + relationship.otherEntityName + "Service.query()\n" +
                    "            .subscribe((res: HttpResponse<"+relationship.otherEntityAngularName+"[]>) => { this." + variableName + " = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));";
        }
        if (variableName && !this.contains(queries, query)) {
            queries.push(query);
            variables.push(variableName + ": " + relationship.otherEntityAngularName + "[];");
        }
    });
    return {
        queries: queries,
        variables: variables,
        hasManyToMany: hasManyToMany
    };
}

/**
 * Function to print a proper array with simple quoted strings
 *
 *  @param {array} array - the array to print
 */
function toArrayString(array) {
    return "[" + array.join('\', \'') + "]";
}

/**
 * Generate a primary key, according to the type
 *
 * @param {any} pkType - the type of the primary key
 * @param {any} prodDatabaseType - the database type
 */
function generateTestEntityId(pkType, prodDatabaseType) {
    if (pkType === 'String') {
        if (prodDatabaseType === 'cassandra') {
            return '\'9fec3727-3421-4967-b213-ba36557ca194\'';
        }
        return '\'123\'';
    }
    return 123;
}