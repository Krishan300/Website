package backend.luna.lehigh.edu;

/**
 * Datum object is the base object for storing our data on the backend. We store an index, title, comment,
 * number of likes, upload date, and like date.
 * DEPRECATED: Now using a handful of new classes to handle objects in multiple tables.
 */
class Datum {
    int index;
    String title;
    String comment;
    int numLikes;
    java.util.Date uploadDate;
    java.util.Date lastLikeDate;
}