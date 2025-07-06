# RPAL Interpreter

## Introduction
**CS 3513 – Programming Languages Module Project**  
As part of the CS 3513 Programming Languages course, our group, **NetCore**, set out to design and implement a fully functional interpreter for RPAL (Right-Polish Arithmetic Language). RPAL is a small, purely functional language often used in academic settings to demonstrate core interpreter design concepts—lexical analysis, parsing, tree rewriting, and runtime evaluation. By building this interpreter end-to-end, we reinforced our understanding of how high-level language constructs are translated into machine behavior. We tackled real-world challenges such as handling nested scopes, managing environments for first-class functions, and evaluating expressions in a stack-based model. Ultimately, this project deepened our grasp of the fundamental techniques that underpin many modern language runtimes.

---

## What We Have Created
We built a complete RPAL interpreter that processes a source file through multiple stages (lexing, parsing, tree conversion) and executes it on a CSE (Control Stack Environment) machine to produce a final integer, boolean, or first-class function (closure) result.

---

## Functionality
1. **Lexical Analysis**
    - Reads raw RPAL code and tokenizes identifiers, literals, operators, and keywords.

2. **Parsing**
    - Uses recursive-descent parsing to construct an Abstract Syntax Tree (AST) representing the program’s structure.

3. **Standard Tree Conversion**
    - Rewrites `let`-bindings into lambda applications and desugars built-in operators to produce a uniform Standard Tree (ST) suitable for evaluation.

4. **CSE Machine Evaluation**
    - Implements a stack-based interpreter that maintains:
        - A **control stack** for ST nodes to evaluate,
        - An **environment stack** for variable bindings and closures,
        - A **result stack** for intermediate values.
    - Executes the ST step-by-step until a final value is obtained.

---
### Group Name - NetCore
## Group Members
- **Vishmitha W.D.L**  220669E
- **Pankaji R.K.K.M.M.S**  220442D

---
