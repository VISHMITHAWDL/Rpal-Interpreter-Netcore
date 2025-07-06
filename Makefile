# ==============================================================================
# Java RPAL Interpreter - Makefile
# ==============================================================================
#
# Instructions:
#   - Open CMD in the project's root directory.
#   - C:\> cd \path\to\RPAL INTERPRETER (containing 'myrpal.java').
#
# Available commands:
#   make compile          - Compiles all Java source files (*.java).
#   make run FILE=<file>  - Runs the program: 'java myrpal input_file' or 'java myrpal -ast input_file'(for Generate Abstract Syntax Tree).
#                           Example: make run FILE=test1.rpal
#   make clean            - Deletes all compiled Java .class files.
#
# ==============================================================================

# Main Java class entry point
MAIN_CLASS = myrpal

# Compile Java source files
compile:
	@echo Compiling Java files...
	javac *.java
	@echo Compilation complete.

# Run Java program with specified input file
run:
	@echo Usage: java $(MAIN_CLASS) $(FILE)
	java $(MAIN_CLASS) $(FILE)
	@echo Usage: java $(MAIN_CLASS) [AST=-ast] $(FILE)
	java $(MAIN_CLASS) $(AST) $(FILE)

# Clean compiled class files recursively
clean:
	@echo Cleaning compiled class files...
	del /S /Q *.class
	@echo Clean complete.

.PHONY: compile run clean
