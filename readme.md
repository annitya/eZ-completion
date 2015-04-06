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

* The Content-class
    - getField(...)
    - getFieldValue(...)

* Clear cache from IDE.
* Toggle assetic-watch

Usage:
------
Configure a PHP-interpreter (local or remote).
Request completions from within literals.


Known issues:
-------------
eZDoc must be placed directly above the variable in question.
You might need to clear the cache before refreshing completions.

Roadmap 1.0.3:
--------------
* Type provider for content-objects.
    - How did Haenchen solve type-hinting in Twig?
    - Find a better name than "annotate".
    - Add cache-warmer to regenerate proxy-classes on cache-clear.
    - See if it is possible to complete text/value-part of fields.
    - What about?
        - fields
        - getFields()
        - getFieldsByLanguage()
    - Be sure to include field-descriptions. (If there are any)
        - https://github.com/JetBrains/intellij-community/blob/master/plugins/properties/src/com/intellij/lang/properties/PropertiesDocumentationProvider.java
    - GIF's of goodness.
    - Adjust expected length of completion-response.
* Make sure that the disable-plugin-setting is honored in all cases.
* Test all cases of type-hinting and content-completion.
* Walkthrough of all changes in 1.0.3. Refactor ftw.

Roadmap 1.0.4:
--------------
* Create eZDoc-intention.
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

Troubleshooting:
----------------
Run the ezcode:completion-command and makes sure that PHP does not output anything else than valid JSON.
Any errors in the console?
