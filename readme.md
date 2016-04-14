Provides code-completion for the eZ-API
---------------------------------------

Installation:
-------------
1. Install plugin.
2. Install the bundle: flageolett/ezcompletionbundle

Requirements:
-------------
PhpStorm 11 or IntelliJ IDEA equivalent.
Bundle: https://github.com/whitefire/ez-completion-bundle
Symfony2-plugin
Remote interpreter support:
 - Remote Php Interpreters
 - SSH Remote-run.

What does it do?
----------------
Provides completion for:

* repository-services:
* query-criteria:
* Available fields for Content (also in Twig)
* Config-resolver

For a complete list take a look in the "Examples" directory within the bundle-repository.

Commands:

* Clear cache from IDE.
* Toggle assetic-watch.
* Refresh completions.

Usage:
------
Configure a PHP-interpreter (local or remote).
Request completions from within literals.
Completions are cached, so remember to refresh completions when necessary.

Troubleshooting:
----------------
Run the ezcode:completion-command and make sure that it does not output anything other than valid JSON.
Any errors in the console?

Known issues:
-------------
You might need to clear the cache before refreshing completions.
Rename-refactoring does not work for @ContentType doc-blocks.

Done:
-----
Added dependence on Symfony2-plugin.
Yaml-completions for:
    - Controllers
    - Views
    - Matchers
    - Keys for the above.
Improved UX for Yaml-Completions
Value completions for:
    - Identifier\ContentType
    - Identifier\ParentContentType
    - Id\ContentType
    - Id\ParentContentType
    - Id\Section
    - Identifier\Section
    - Id\ContentTypeGroup
Support multiple values for matchers.
Guess content-type in twig by using matchers in yml-files.

Current:
--------
* The Symfony2-plugin is evolving rather quickly. Reconsider dependencies.

Roadmap 1.0.4:
--------------
* Verify behaviour if one or more optional dependencies are missing.
* Test all completions.
* Update changelist.

Roadmap 1.0.5:
--------------
* Look into handleElementRename(...) and see if it might solve refactoring.
* Might it be possible to make completions fully searchable?
    - Easily noticed when completing numerics.
* Replacing numeric-completions is cumbersome.
* Replace usages of the ContentClass-name with its correct equivalent ContentType
* Allow user to select ContentType if multiple are detected.
* Completions for supported template-scopes (global, sitaccess etc...)
* Donut?
* Make use of completion-confidence for auto-completion when applicable.
* Add better error-message if Bundle is missing.
* context.getEditor().getCaretModel().getPrimaryCaret().moveCaretRelatively(2, 0, false, false);
    - This one needs to get smarter. Check all completions and take parameters into consideration.
* Walk through initial-install and see if the UX can be improved.
* Goto definitions for ezsettings. (gotoSymbolContributor?)
* Goto definitions for yaml-classes/definitions.
* Inspections for field-accessors.
* Add CompletionConfidence (order="before javaSkipAutopopupInStrings")
* Streamline insertion of completions.
* Support completions for multiple-values for criteria.
* Display FieldType in field-completion lookup.
* Make data-provider for completions abstract.
* Data-duplication in the completion-bundle is getting ridiculous.
* Provide better progress-indicators for commands
    - Time based (beware of yml-changes)
    - Activity-indicator for watch.
* Make better use of processingContext.
* Type providers for fields in twig.
* Automatic eZDoc if possible.
    - Direct sql-access through plugin.
        - loadContent => resolve content-type.
    - Does database access yield other possibilities as well?
        - Database is being difficult, postponed for now.
* Provide an ad-hoc way to search for ContentTypes and Fields.
* Execute SearchService-query:
    - Add support for services.
    - Ask for unresolved criteria-values.
    - Modify method to return eval'd data.
    - Present the results in an easily browsible manner.
