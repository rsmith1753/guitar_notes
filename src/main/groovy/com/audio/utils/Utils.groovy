package audio.utils

class Utils {

    static String frequencyToNote(double frequency) {
        if (frequency <= 0) return "Unknown"

        String[] notes = ["C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"]
        int A4 = 440 // Reference frequency
        int semitonesFromA4 = (int) Math.round(12 * Math.log(frequency / A4) / Math.log(2))
        int noteIndex = (semitonesFromA4 + 9) % 12 // Shift to align with A = 440Hz
        int octave = (int) (4 + ((semitonesFromA4 + 9) / 12)) // Adjust octave number

        return "${notes[noteIndex]}${octave}"
    }

}
