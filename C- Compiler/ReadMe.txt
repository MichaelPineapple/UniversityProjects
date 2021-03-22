NAME: Michael Wilson
N-NUMBER: n01223646
TITLE: C- Compiler (Project 4)
COURSE: COP4620 - Construction of Language Translators
INSTRUCTOR: Roger E. Eggen
DATE: 4-11-19

> Generates tokens and handles comments for the high-level lanaguage C-
It does this by reading the source code from a file and running each line through a custom built state machine.
The machine handles errors and generates appropriate tokens for the code while ignoring multi-line, embedded, and single line comments.

> Uses the tokens from the lexical analyzer to check for syntax errors using top down LL(1) parsing.

> Checks for semantic errors with guidance from the grammar.

> Outputs intermediate code to the command line

The input file name is passed to command line argument one.
This program does not generate any files.