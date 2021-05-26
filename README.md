# ChimeneProject0
Some design notes.
Base resource classes are user, transaction and account.
User has a boolean isEmployee which acts as a permissions flag to allow the user to access employee methods. 
Account has a boolean isPending which acts similarly to differentiate pending and active accounts. 
LedgerService handles the actual transactions between accounts and creating/destroying accounts. 
Menu is built on top of LedgerEngine and works to display the ui and take user input. 
Driver simply navigates betweeen the different menus represented by the Menu methods. 
Currently no restrictions on username/password other than just being non-empty strings. 