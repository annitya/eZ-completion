Provides code-completion for the eZ-API
---------------------------------------

Installation:
-------------
1. Install the excellent Symfony2-plugin
2. Install this plugin.
3. Install this bundle: https://github.com/whitefire/ez-completion-bundle

What does it do?
----------------
Provides completion for:
    * ContentTypeService
    // LanguageService
    // FieldTypeService
    // ObjectStateService

Usage:
------

Request completions from within literals.

Todo:
-----
* Validate location of console-executable (consoleProvider)
* Support for remote consoleProviders using either deployment settings or selected server.
* Button for refreshing cache-completion?
* Clear-cache button with support for remote servers.
* Assetic-watch toggle with support for remote servers.
* Add twig-completions for content/location
* Implment psi-pattern-matcher for completion-contributor.
* Add completions for search-critera.
* Use PSR-4 for bundle-autoloading.
* The logic for resolving completion-types is somewhat naive. Lets try to resolve the identifier.
* Completions for a method is returned for all parameters, regardles of parameter-index.

Known bugs:
----------
String-completions will always be in single quotes.
Identifiers which is parsable as integers will be returned without quotes.
Only the ContentTypeService-completions are working right now.

Troubleshooting:
----------------
Run the ezcode:completion-command and makes sure that PHP does not output anything else than valid JSON.
Any erros in the console?
