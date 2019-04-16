# Transcription
This is a configurable GUI which automatically transcribes (NOT translates) your plain keyboard into other languages.

# How to use it
When you open up the JAR, you'll see 4 lines.

1st Line: This is the language line. Here, enter the language or the language code. For example, "greek" or "el" (Greek language code).

2nd Line: This is the input line. Here, type up whatever you want to transcribe, within the boundaries of the language of course.

3rd Line: This is the output line. Here is where the transcribed text will be.

4th Line: A non-editable "error line" which tracks information about an exception.

A good chunk of this is stored in the /config/ folder.

There's the "init" file which follows the format: [<aliases array>:<text file>]

In each language file, order matters a lot. The input line is basically run through a list of replace() commands, so [sh] should come before [s].

There are also special $ lines which basically are for entries which require spaces. For example, in Greek, lowercase sigma (σ/ς) will be ς if it's the final letter, and σ otherwise.

"#" are comment lines. Nothing much to it.

Most of this can be figured out by looking at the existing examples.
