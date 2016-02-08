Provides code-completion for the eZ-API
---------------------------------------

Installation:
-------------
1. Install plugin.
2. Install the bundle:

Requirements:
-------------
PhpStorm 10 or IntelliJ IDEA equivalent.
Bundle: https://github.com/whitefire/ez-completion-bundle
Remote interpreter support:
 - Remote Php Interpreters
 - SSH Remote-run.

While the Symfony2-plugin is not a hard requirement, it is highly recommended.
Much of the provided functionality will be unavailable without it.

What does it do?
----------------
Provides completion for:

* services:
 - ContentTypeService
 - LanguageService
 - FieldTypeService
 - ObjectStateService
 - RoleService
 - SectionService
 - UrlAliasService

* criteria:
 - ContentTypeId
 - ContentTypeIdentifier
 - ContentTypeGroupId
 - LanguageCode
 - ObjectStateId
 - SectionId

* The Content-class
    - fields/getFields
    - getFieldValue(...)

* Config-resolver

* Clear cache from IDE.
* Toggle assetic-watch

Usage:
------
Configure a PHP-interpreter (local or remote).
Request completions from within literals.

Troubleshooting:
----------------
Run the ezcode:completion-command and make sure that it does not output anything other than valid JSON.
Any errors in the console?

Known issues:
-------------
You might need to clear the cache before refreshing completions.
Rename-refactoring does not work for @ContentType doc-blocks.
No completion for fields returned from getFieldsByLanguage because they are not indexed by identifier.

Current:
--------
// Attach test-file which lists all completion/type-resolve cases.
// Inspections for field-accessors.


Roadmap 1.0.3:
--------------
* Update changelog.
    - GIF's of goodness.

Roadmap 1.0.4:
--------------

* Yml-completions for controllers, matchers, views, etc...

* Goto definitions for ezsettings. (gotoSymbolContributor?)
* Execute SearchService-query:
    - Add support for services.
    - Ask for unresolved criteria-values.
    - Modify method to return eval'd data.
    - Present the results in an easily browsible manner.
* Twig
    - completions for content/location
    - How does the Symfony2-plugin solve type-hinting in Twig?
    - The hard way. There are no types in twig.
    - ez_field_value-helper?
* Donut?
* Automatic eZDoc if possible.
    - Direct sql-access through plugin.
        - loadContent => resolve content-type.
    - Does database access yield other possibilities as well?
        - Database is being difficult, postponed for now.
