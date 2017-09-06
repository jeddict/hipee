  function generateEntityQueries(relationships, entityInstance, dto) {
        var queries = [];
        var variables = [];
        var hasManyToMany = false;
        relationships.forEach(function(relationship) {
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
                    "!"+ relationshipFieldName + " || !"+ relationshipFieldName+".id" :
                    "!"+ relationshipFieldName+"Id";

                query = "this."+relationship.otherEntityName+"Service\n" +
"            .query({filter: '"+relationship.otherEntityRelationshipName.toLowerCase()+"-is-null'})\n"+
"            .subscribe((res: ResponseWrapper) => {\n"+
"                if ("+relationshipFieldNameIdCheck+") {\n"+
"                    this."+ variableName + " = res.json;\n"+
"                } else {\n"+
"                    this."+relationship.otherEntityName+"Service\n"+
"                        .find("+relationshipFieldName + (dto === 'no' ? '.id' : 'Id') + ")\n"+
"                        .subscribe((subRes: "+relationship.otherEntityAngularName+") => {\n"+
"                            this."+variableName+" = [subRes].concat(res.json);\n"+
"                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));\n"+
"                }\n"+
"            }, (res: ResponseWrapper) => this.onError(res.json));";
            } else if (relationship.relationshipType !== 'one-to-many') {
                variableName = relationship.otherEntityNameCapitalizedPlural.toLowerCase();
                if (variableName === entityInstance) {
                    variableName += 'Collection';
                }
                query = "this."+relationship.otherEntityName+"Service.query()\n"+
"            .subscribe((res: ResponseWrapper) => { this."+variableName+" = res.json; }, (res: ResponseWrapper) => this.onError(res.json));";
            }
            if (variableName && !this.contains(queries, query)) {
                queries.push(query);
                variables.push(variableName+": "+ relationship.otherEntityAngularName+"[];");
            }
        });
        return {
            queries : queries,
            variables : variables,
            hasManyToMany : hasManyToMany
        };
    }

    /**
     * Function to print a proper array with simple quoted strings
     *
     *  @param {array} array - the array to print
     */
    function toArrayString(array) {
        return "["+array.join('\', \'')+"]";
    }
