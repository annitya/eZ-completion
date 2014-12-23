Provides code-completion for the eZ-API
---------------------------------------

Installation:
-------------
1. Install plugin.
3. Install the bundle: https://github.com/whitefire/ez-completion-bundle

What does it do?
----------------
Provides completion for:
    * ContentTypeService
    * LanguageService
    * FieldTypeService
    * ObjectStateService
    * RoleService
    * SectionService
    * UrlAliasService

Usage:
------
Request completions from within literals.
Completion is case-sensitive.

Todo:
-----
* Validate location of console-executable (consoleProvider)
* Support for remote consoleProviders using either deployment settings or selected server.
* Create cusom toolbar with buttons.
* Clear-cache button with support for remote servers.
* Assetic-watch toggle with support for remote servers.
* Add twig-completions for content/location
* Add completions for search-critera.
* Use PSR-4 for bundle-autoloading.
* Perhaps documentation-lookups could be helpful?
* Should I worry about environment for the console-executable?
* Refresh-method is icky.

Known issues:
----------
String-completions will always be in single quotes.
Identifiers which is parsable as integers will be returned without quotes.

Troubleshooting:
----------------
Run the ezcode:completion-command and makes sure that PHP does not output anything else than valid JSON.
Any erros in the console?
