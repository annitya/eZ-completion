Provides code-completion for the eZ-API
---------------------------------------

Installation:
-------------
1. Install plugin.
2. Install the bundle:

Requirements:
-------------
PhpStorm 8.0.2 or IntelliJ IDEA equivalent.
Bundle: https://github.com/whitefire/ez-completion-bundle
Plugins:
 - Remote Run
 - Remote Php Interpreters
 - SSH Remote-run.

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

* Clear cache from IDE.

Usage:
------
Configure a PHP-interpreter (local or remote).
Request completions from within literals.
Completion is case-sensitive (for now).

Todo:
-----
* Assetic-watch toggle.
* Add twig-completions for content/location
* Perhaps documentation-lookups could be helpful?
* Automatic installation of required bundle.
* SearchService:
    - execute query and return data with information regarding query-performance.
* Bundle:
    - Use PSR-4 for bundle-autoloading.
    - Automatic installation?
* Remote logs?
* Donut?

Known issues:
-------------
You might need to clear the cache before refreshing completions.

Troubleshooting:
----------------
Run the ezcode:completion-command and makes sure that PHP does not output anything else than valid JSON.
Any errors in the console?
