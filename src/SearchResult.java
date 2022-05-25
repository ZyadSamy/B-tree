public class SearchResult implements ISearchResult {
    public String title;
    String id;
    int rank;
    String url;
    String content;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRank() {
        return this.rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
