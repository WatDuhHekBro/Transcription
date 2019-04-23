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

# Other
- Arabic's pharyngealized consonants can be accessed either through a doubled consonant (tt) or by capitalizing that letter (T).
- I'm not sure how valid the h sound is in modern Greek. I looked at how words like "heliocentric" were originally written.
- The amount of kanji available is limited because my knowledge of kanji is limited. Anyways, you access kanji by using a single quote before a key word ('). For example, 'gaku -> 学.
- I know very little of how Hebrew works, so expect it to be clunky. For example, shalom is transcribed via "shlvm". Yeah...
- For Hindi, I'm assuming that the diacritic for silencing the syllable (pa -> p) is used except for the final letter/syllable of a word. I do not know if this is actually the case.
- Also, my method of applying those silencing diacritics for Hindi is by using null characters which (normally) can't be typed out. Use your arrow keys or a program like Notepad++.
- Serbian can be exchanged with latin_extended, so consequently you have to know the Latin alphabet for Serbian to get the Cyrillic version for Serbian.
- For Chinese, you can only get so far with pinyin readings. So if I ever get around to it, I'm hoping to add a hybrid radicals/strokes system.
- For Russian, with the exception of я, ё, and ю, y = ы and j = й. Also, ^e = э because of its rarity.
- For Vietnamese, you can change/remove a diacritic by just pressing another diacritic key. No need to backspace a character and redo its modifications. All the modifier keys are around where your right pinky is. And none of these are just a letter with a combining diacritic. I'm not going to stoop that low.
- For Korean, the second bar is how you enter syllables and the third bar is your combined output. Examples are better, so if you wanted to type out 한, you would type "han" (ㅎㅏㄴ), and then press [Space] or [Enter] to send it to the output glued together into one character. You can also press the grave/tilde key (`/~) to get hanja. 안녕하세요 would be "an" (ㅇㅏㄴ), "nyuung" (ㄴㅕㅇ), "ha" (ㅎㅏ), "se" (ㅅㅔ), "yo" (ㅇㅛ). Your input bar must be 2 or 3 characters for the combination function to work.
- For Korean, you can also backspace when your input bar is empty to backspace the output.
- The universal separator is "|". For example, if you're trying to get the letters for /t/ and /h/ instead of /th/, you can enter "t|h".

# VI
- The semicolon key (;) is the all-in-one modifier key. It contains [â ă ê ô ơ ư đ].
- The quote key (') is the rising diacritic. [á]
- The left bracket key ([) is the falling diacritic. [à]
- The right bracket key (]) is the sharp rising diacritic. [ã]
- The comma key (,) is the hook diacritic. [ả]
- The period key (.) is the sharp falling diacritic. [ạ]

# KO
- ㅐ is "aa" instead of "ae". I find that the "a" in "apple" would look like a sharp a or a double a.
- ㅓ is "uu" instead of "eo". Personally, it makes more sense for the "uh" sound to be closer to "u".
- ㅡ is "yy" instead of "eu". Same reason as "uu"/"eo", "yy" more closely resembles ы which, according to the IPA, has the exact same sound as this vowel.
