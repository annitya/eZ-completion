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

* Clear cache from IDE.
* Toggle assetic-watch

Usage:
------
Configure a PHP-interpreter (local or remote).
Request completions from within literals.

Troubleshooting:
----------------
Run the ezcode:completion-command and makes sure that it does not output anything else than valid JSON.
Any errors in the console?

Known issues:
-------------
You might need to clear the cache before refreshing completions.
Rename-refactoring does not work for @ContentType doc-blocks.
No completion for fields returned from getFieldsByLanguage because they are not indexed by identifier.

Roadmap 1.0.3:
--------------
* Wait until all other jobs are completed before fetching completions?
* Does the "disabled by default" actually work?
* Update changelog.
* Type provider for content-objects.
    - GIF's of goodness.
* Test all cases of type-hinting and content-completion.
    - Do some internal testing at Keyteq.
        - Look for broken type-chains.
        - Errors while using the plugin?
        - Other problems and suggestions.
* Add bundle to packagist.

Roadmap 1.0.4:
--------------
* Subscription-model for the CompletionContainer so that the type-providers don't have to fetch it all the time?
* Inspections for field-accessors.
* ez_field_value-helper? (twig)
* Determine if configured environment should be tied to selected interpreter.
* Yml-completions for controllers, matchers, views, etc...
* Add completions to entities returned by the API-repository.
    - Ex: ContentType returned from loadContentType...(...)
* How does the Symfony2-plugin solve type-hinting in Twig?
* Create eZDoc-intention.
* Goto definitions for ezsettings. (gotoSymbolContributor?)
* Execute SearchService-query:
    - Add support for services.
    - Ask for unresolved criteria-values.
    - Modify method to return eval'd data.
    - Present the results in an easily browsible manner.
* Bundle:
    - Use PSR-4 for bundle-autoloading.
* Revisit the whole bundle-installation-thingy.
* Add twig-completions for content/location
* Donut?
* Automatic eZDoc if possible.
    - Direct sql-access through plugin.
        - loadContent => resolve content-type.

