Provides code-completion for the eZ-API
---------------------------------------

Installation:
-------------
1. Install plugin.
3. Install the bundle: https://github.com/whitefire/ez-completion-bundle

What does it do?
----------------
Provides completion for
services:
    * ContentTypeService
    * LanguageService
    * FieldTypeService
    * ObjectStateService
    * RoleService
    * SectionService
    * UrlAliasService

criteria:
    * ContentTypeId
    * ContentTypeIdentifier
    * ContentTypeGroupId
    * LanguageCode
    * ObjectStateId
    * SectionId

Usage:
------
Request completions from within literals.
Completion is case-sensitive (for now).

Todo:
-----

* Clear-cache button.
* Assetic-watch toggle.
* Add twig-completions for content/location
* Perhaps documentation-lookups could be helpful?
* Automatic installation of required bundle.
* Settings:
    - language must be configurable. Use count max of contenobject-languages as default?
    - validate console-executable.
    - support for remote servers.
    - force environment for console-executable.
* SearchService:
    - execute query and return data with information regarding query-performance.
* Bundle:
    - Use PSR-4 for bundle-autoloading.
    - Proper separation of sources and completions.
    - Automatic installation?

Known issues:
-------------
You might need to clear the cache before refreshing completions.

Troubleshooting:
----------------
Run the ezcode:completion-command and makes sure that PHP does not output anything else than valid JSON.
Any errors in the console?
