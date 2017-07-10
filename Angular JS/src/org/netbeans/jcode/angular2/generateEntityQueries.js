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

                query =
        "this."+relationship.otherEntityName+"Service" +
            ".query({filter: '"+relationship.otherEntityRelationshipName.toLowerCase()+"}-is-null'})"+
            ".subscribe((res: ResponseWrapper) => {"+
                "if ("+relationshipFieldNameIdCheck+") {"
                    "this."+ variableName + " = res.json;"+
                "} else {"+
                    "this."+relationship.otherEntityName+"Service"+
                        ".find("+relationshipFieldName + (dto === 'no' ? '.id' : 'Id') + ")"+
                        ".subscribe((subRes: "+relationship.otherEntityAngularName+") => {"+
                            "this."+variableName+" = [subRes].concat(res.json);"+
                        "}, (subRes: ResponseWrapper) => this.onError(subRes.json));"+
                "}"+
            "}, (res: ResponseWrapper) => this.onError(res.json));";
            } else if (relationship.relationshipType !== 'one-to-many') {
                variableName = relationship.otherEntityNameCapitalizedPlural.toLowerCase();
                if (variableName === entityInstance) {
                    variableName += 'Collection';
                }
                query =
        "this."+relationship.otherEntityName+"Service.query()"+
            ".subscribe((res: ResponseWrapper) => { this."+variableName+" = res.json; }, (res: ResponseWrapper) => this.onError(res.json));";
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
