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
Remote interpreter support:
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
* Toggle assetic-watch

Usage:
------
Configure a PHP-interpreter (local or remote).
Request completions from within literals.

Roadmap 1.0.3:
--------------
* Type provider for content-objects.
    - Should make completions for getField and getFieldValue possible.
    - See if it is possible to complete text/value-part of fields.
    - Annotation-support.
    - Be sure to include field-descriptions.
    - GIF's of goodness.
    - Adjust expected length of completion-response.
    - Be sure that the disable-plugin-setting is honoured in all cases.

Roadmap 1.0.4:
--------------
* Goto definitions for ezsettings. (gotoSymbolContributor?)
* Execute SearchService-query:
    - Add support for services.
    - Ask for unresolved criteria-values.
    - Modify method to return eval'd data.
    - Present the results in an easily browsible manner.
* Bundle:
    - Use PSR-4 for bundle-autoloading.
    - Add to packagist.
* Revisit the whole bundle-installation-thingy.
* Add twig-completions for content/location
* Donut?
* https://confluence.jetbrains.com/display/PhpStorm/PHP+Open+API#PHPOpenAPI-PhpTypeProvider
* https://devnet.jetbrains.com/message/5520264#5520264

Future:
-------
* Are there challenges with starting a new eZ-site which this plugin could help with?

Known issues:
-------------
You might need to clear the cache before refreshing completions.

Troubleshooting:
----------------
Run the ezcode:completion-command and makes sure that PHP does not output anything else than valid JSON.
Any errors in the console?
