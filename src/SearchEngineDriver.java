import java.util.List;

public class SearchEngineDriver {
    public static void main(String[] args) {
        SearchEngine yes = new SearchEngine();
        yes.indexWebPage("Wikipedia Data Sample/wiki_00");
        System.out.println();
        List<ISearchResult> found = yes.searchByMultipleWordWithRanking("the is");
        System.out.println();
    }
}
