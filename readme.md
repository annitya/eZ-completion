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

* Assetic-watch toggle.
* Add twig-completions for content/location
* Perhaps documentation-lookups could be helpful?
* Automatic installation of required bundle.
* Settings:
    - validate console-executable.
    - support for remote environments.
    - force environment for console-executable.
* SearchService:
    - execute query and return data with information regarding query-performance.
* Bundle:
    - Use PSR-4 for bundle-autoloading.
    - Automatic installation?
* Add MIT license.
* Donut?
* Better notifications if fetch of completions fails.
* Refresh available languages on apply in preferences.

Known issues:
-------------
You might need to clear the cache before refreshing completions.

Troubleshooting:
----------------
Run the ezcode:completion-command and makes sure that PHP does not output anything else than valid JSON.
Any errors in the console?
