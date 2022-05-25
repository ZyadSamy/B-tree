public class SearchEngineDriver {
    public static void main(String[] args) {
        SearchEngine yes = new SearchEngine();
        yes.indexWebPage("Wikipedia Data Sample/wiki_00");
        System.out.println();
        yes.deleteWebPage("Wikipedia Data Sample/wiki_00");
        System.out.println();
    }
}
