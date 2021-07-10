package advisor;

public class MusicEntry {
    String name;
    String artist;
    String reference;


    public void setName(String name) {
        this.name = name;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return name + "\n" + artist + "\n" + reference;
    }
}