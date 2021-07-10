package advisor;

import java.util.ArrayList;
import java.util.List;

public class PlaylistPages<T> {
    List<T> responsePages;
    int currentPage = 0;
    int entriesLimit = Integer.parseInt(Main.comLineArgs.get("-page"));

    public PlaylistPages() {
        this.responsePages = new ArrayList<>();
    }

    public void addMusicEntry(T musicEntry) {
        responsePages.add(musicEntry);
    }

    public void printCurrent() {
        int entriesNumber = responsePages.size();
        int lastEntries = entriesNumber % entriesLimit;
        int maxPageNum = entriesNumber / entriesLimit + (lastEntries > 0 ? 1 : 0);


        int startEntry = currentPage * entriesLimit;
        if (currentPage == maxPageNum - 1 && lastEntries > 0) {
            for (int i = startEntry; i < startEntry + lastEntries; i++) {
                System.out.println(responsePages.get(i));
            }
        } else {
            int limitEntry = currentPage * entriesLimit + entriesLimit;
            for (int i = startEntry; i < limitEntry; i++) {
                System.out.println(responsePages.get(i));
            }
        }
        System.out.printf("---PAGE %d OF %d---", currentPage + 1, maxPageNum);
        System.out.println();

    }

    public void printNext() {
        int entriesNumber = responsePages.size();
        int lastEntries = entriesNumber % entriesLimit;
        int maxPageNum = entriesNumber / entriesLimit + (lastEntries > 0 ? 1 : 0);

        if (currentPage < maxPageNum - 1) {
            currentPage++;
            printCurrent();
        } else {
            System.out.println("No more pages.");
        }
    }

    public void printPrev() {
        if (currentPage > 0) {
            currentPage--;
            printCurrent();
        } else {
            System.out.println("No more pages.");
        }
    }

}