Provides code-completion for the eZ-API
---------------------------------------

Installation:
-------------
1. Install PhpStorm plugin.
2. Install this bundle: https://github.com/whitefire/ez-completion-bundle

What does it do?
----------------
Provides completion for:
    * ContentTypeService
    * LanguageService
    * FieldTypeService
    * ObjectStateService

Usage:
------

Request completions from within literals.

Todo:
-----
* Validate location of console-executable (consoleProvider)
* Support for remote consoleProviders using either deployment settings or selected server.
* Button for refreshing cache-completion.
* Clear-cache button with support for remote servers.
* Assetic-watch toggle with support for remote servers.
* Auto-poup completions if possible.
* Add twig-completions for content/location
* Implment psi-pattern-matcher for completion-contributor.
* Add completions for search-critera.
* Use PSR-4 for bundle-autoloading.
* Extract method from extend in Framework.eZCompletionContributor-constructor.
* I have a feeling that my "generic" method in CompletionContainer could be more elegant.
* The logic for resolving completion-types is somewhat naive. Lets try to resolve the identifier.

Known bugs:
----------
String-completions will always be in single quotes.