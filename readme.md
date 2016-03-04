Provides code-completion for the eZ-API
---------------------------------------

Installation:
-------------
1. Install plugin.
2. Install the bundle: flageolett/ezcompletionbundle

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

* repository-services:
* query-criteria:
* Available fields for Content (also in Twig)
* Config-resolver

For a complete list take a look in the "Examples" directory within the bundle-repository.

Commands:

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

Current:
--------
Begin work on 1.0.4.

Roadmap 1.0.4:
--------------

* Yml-completions for controllers, matchers, views, etc...
    - Might also make it possible to guess ContentTypes in templates.
* Goto definitions for ezsettings. (gotoSymbolContributor?)
* Donut?
* Automatic eZDoc if possible.
    - Direct sql-access through plugin.
        - loadContent => resolve content-type.
    - Does database access yield other possibilities as well?
        - Database is being difficult, postponed for now.
* Inspections for field-accessors.
* Add CompletionConfidence (order="before javaSkipAutopopupInStrings")
* Streamline insertion of completions.
* Support completions for multiple-values for criteria.
* Display FieldType in field-completion lookup.
* Make data-provider for completions abstract.
* Data-duplication in the completion-bundle is getting ridiculous.
* Provide better progress-indicators for commands
    - Time based (beware of yml-changes)
    - Acitivity-indicator for watch.
* Make better use of processingContext.
* Type providers for fields in twig.

Roadmap 1.0.5:
--------------
* Provide an ad-hoc way to search for ContentTypes and Fields.
* Make more use of PlatformPatterns.
* Execute SearchService-query:
    - Add support for services.
    - Ask for unresolved criteria-values.
    - Modify method to return eval'd data.
    - Present the results in an easily browsible manner.
