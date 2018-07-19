/**
 * Copyright 2013-2018 the original author or authors from the JHipster project.
 *
 * This file is part of the JHipster project, see https://www.jhipster.tech/
 * for more information.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Portions Copyright 2013-2018 Gaurav Gupta
 */

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
                    "            .subscribe((res: HttpResponse<I"+relationship.otherEntityAngularName+"[]>) => {\n" +
                    "                if (" + relationshipFieldNameIdCheck + ") {\n" +
                    "                    this." + variableName + " = res.body;\n" +
                    "                } else {\n" +
                    "                    this." + relationship.otherEntityName + "Service\n" +
                    "                        .find(" + relationshipFieldName + (dto === 'no' ? '.id' : 'Id') + ")\n" +
                    "                        .subscribe((subRes: HttpResponse<I" + relationship.otherEntityAngularName + ">) => {\n" +
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
                    "            .subscribe((res: HttpResponse<I"+relationship.otherEntityAngularName+"[]>) => { this." + variableName + " = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));";
        }
        if (variableName && !this.contains(queries, query)) {
            queries.push(query);
            variables.push(variableName + ": I" + relationship.otherEntityAngularName + "[];");
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

    /**
     * Generate Entity Client Field Declarations
     *
     * @param {string} pkType - type of primary key
     * @param {Array|Object} fields - array of fields
     * @param {Array|Object} relationships - array of relationships
     * @param {string} dto - dto
     * @returns variablesWithTypes: Array
     */
    function generateEntityClientFields(pkType, fields, relationships, dto) {
        var variablesWithTypes = [];
        let tsKeyType;
        if (pkType === 'String') {
            tsKeyType = 'string';
        } else {
            tsKeyType = 'number';
        }
        variablesWithTypes.push('id?: '+tsKeyType);
        fields.forEach(function(field) {
            var fieldType = field.fieldType;
            var fieldName = field.fieldName;
            var tsType;
            if (field.fieldIsEnum) {
                tsType = fieldType;
            } else if (fieldType === 'Boolean') {
                tsType = 'boolean';
            } else if (['Integer', 'Long', 'Float', 'Double', 'BigDecimal'].includes(fieldType)) {
                tsType = 'number';
            } else if (fieldType === 'String' || fieldType === 'UUID') {
                tsType = 'string';
            } else if (['LocalDate', 'Instant', 'ZonedDateTime'].includes(fieldType)) {
                tsType = 'Moment';
            } else { // (fieldType === 'byte[]' || fieldType === 'ByteBuffer') && fieldTypeBlobContent === 'any' || (fieldType === 'byte[]' || fieldType === 'ByteBuffer') && fieldTypeBlobContent === 'image' || fieldType === 'LocalDate'
                tsType = 'any';
                if (['byte[]', 'ByteBuffer'].includes(fieldType) && field.fieldTypeBlobContent !== 'text') {
                    variablesWithTypes.push(fieldName+'ContentType?: string');
                }
            }
            variablesWithTypes.push(fieldName+'?: '+tsType);
        });

        relationships.forEach(function(relationship) {
            var fieldType;
            var fieldName;
            var relationshipType = relationship.relationshipType;
            if (relationshipType === 'one-to-many' || relationshipType === 'many-to-many') {
                fieldType = 'I'+relationship.otherEntityAngularName+'[]';
                fieldName = relationship.relationshipFieldNamePlural;
            } else if (dto === 'no') {
                fieldType = 'I'+relationship.otherEntityAngularName;
                fieldName = relationship.relationshipFieldName;
            } else {
                const relationshipFieldName = relationship.relationshipFieldName;
                const relationshipFieldNamePlural = relationship.relationshipFieldNamePlural;
                const relationshipType = relationship.relationshipType;
                const otherEntityFieldCapitalized = relationship.otherEntityFieldCapitalized;
                const ownerSide = relationship.ownerSide;

                if (relationshipType === 'many-to-many' && ownerSide === true) {
                    fieldType = 'I'+otherEntityFieldCapitalized+'[]';
                    fieldName = relationshipFieldNamePlural;
                } else if (relationshipType === 'many-to-one' || (relationshipType === 'one-to-one' && ownerSide === true)) {
                    if (otherEntityFieldCapitalized !== 'Id' && otherEntityFieldCapitalized !== '') {
                        fieldType = 'string';
                        fieldName = relationshipFieldName+otherEntityFieldCapitalized;
                        variablesWithTypes.push(fieldName+'?: '+fieldType);
                    }
                    fieldType = 'number';
                    fieldName = relationshipFieldName+'Id';
                } else {
                    fieldType = tsKeyType;
                    fieldName = relationshipFieldName+'Id';
                }
            }
            variablesWithTypes.push(fieldName+'?: '+fieldType);
        });
        return variablesWithTypes;
    }

    /**
     * Generate Entity Client Imports
     *
     * @param {Array|Object} relationships - array of relationships
     * @param {string} dto - dto
     * @returns typeImports: Map
     */
    function generateEntityClientImports(relationships, dto) {
        var clientFramework = 'angularX';
        const typeImports = [];
        relationships.forEach(function(relationship) {
            const relationshipType = relationship.relationshipType;
            let toBeImported = false;
            if (relationshipType === 'one-to-many' || relationshipType === 'many-to-many') {
                toBeImported = true;
            } else if (dto === 'no') {
                toBeImported = true;
            } else {
                const ownerSide = relationship.ownerSide;

                if (relationshipType === 'many-to-many' && ownerSide === true) {
                    toBeImported = true;
                }
            }
            if (toBeImported) {
                const otherEntityAngularName = relationship.otherEntityAngularName;
                const importType = 'I'+otherEntityAngularName;
                let importPath;
                if (otherEntityAngularName === 'User') {
                    importPath = clientFramework === 'angularX' ? 'app/core/user/user.model' : './user.model';
                } else {
                    importPath = './'+relationship.otherEntityFileName+'.model';
                }
                var typeImport = {};
                typeImport.importedType = importType;
                typeImport.importedPath = importPath;
                typeImports.push(typeImport);
                
            }
        });
        return typeImports;
    }

    /**
     * Generate Entity Client Field Default Values
     *
     * @param {Array|Object} fields - array of fields
     * @returns {Array} defaultVariablesValues
     */
    function generateEntityClientFieldDefaultValues(fields) {
        const defaultVariablesValues = {};
        fields.forEach(function(field) {
            const fieldType = field.fieldType;
            const fieldName = field.fieldName;
            if (fieldType === 'Boolean') {
                defaultVariablesValues[fieldName] = 'this.'+fieldName+' = false;';
            }
        });
        return defaultVariablesValues;
    }
    
    function upperFirstCamelCase(value) {
        return upperFirst(camelCase(value));
    }

    function upperFirst(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
    }

    function camelCase(str) {
        return str.replace(/(?:^\w|[A-Z]|\b\w)/g,
                function (letter, index) {
                    return index === 0 ? letter.toLowerCase() : letter.toUpperCase();
                }
        ).replace(/\s+/g, '');
    }