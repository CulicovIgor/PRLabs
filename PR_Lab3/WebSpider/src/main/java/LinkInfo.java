public class LinkInfo implements Comparable {
    private String link;
    private int level;
    private int count;
    private boolean isVisited;

    public LinkInfo(String link, int level) {
        this.link = link;
        this.level = level;
        this.count = 0;
        this.isVisited = false;
    }

    public LinkInfo(String link, int level, int count) {
        this.link = link;
        this.level = level;
        this.count = count;
        this.isVisited = true;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int keywordCount) {
        this.count = keywordCount;
    }

    @Override
    public int hashCode() {
        return link.hashCode();
    }

    public int compareTo(Object obj) {
        if (!(obj instanceof LinkInfo))
            return -1;
        LinkInfo foundLink = (LinkInfo) obj;
        return link.compareTo(foundLink.getLink());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LinkInfo))
            return false;
        return obj == this || ((LinkInfo) obj).getLink().equals(link);
    }
}

