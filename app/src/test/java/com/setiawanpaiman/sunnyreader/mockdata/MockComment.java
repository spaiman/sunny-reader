package com.setiawanpaiman.sunnyreader.mockdata;

import com.setiawanpaiman.sunnyreader.data.model.Comment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockComment {

    public static Comment COMMENT1 = create(1L, 10239231L, "Author Comment 1", "Comment 1", 10L, new ArrayList<Long>(), false);

    // Nested comments
    public static Comment COMMENT2 = create(2L, 10239232L, "Author Comment 2", "Comment 2", 20L, Arrays.asList(21L, 22L), false);
    public static Comment COMMENT21 = create(21L, 102392321L, "Author Comment 21", "Comment 21", 210L, Arrays.asList(211L), false);
    public static Comment COMMENT211 = create(211L, 1023923211L, "Author Comment 211", "Comment 211", 2110L, new ArrayList<Long>(), false);
    public static Comment COMMENT22 = create(22L, 102392322L, "Author Comment 22", "Comment 22", 220L, Arrays.asList(221L, 222L), false);
    public static Comment COMMENT221 = create(221L, 1023923221L, "Author Comment 221", "Comment 221", 2210L, new ArrayList<Long>(), false);
    public static Comment COMMENT222 = create(222L, 1023923222L, "Author Comment 222", "Comment 222", 2220L, new ArrayList<Long>(), false);

    // Nested comments with a deleted comment
    public static Comment COMMENT3 = create(3L, 10239233L, "Author Comment 3", "Comment 3", 30L, Arrays.asList(31L, 32L), false);
    public static Comment COMMENT31 = create(31L, 102392331L, "Author Comment 31", "Comment 31", 310L, Arrays.asList(311L, 312L), true);
    public static Comment COMMENT32 = create(32L, 102392332L, "Author Comment 32", "Comment 32", 320L, Arrays.asList(321L), false);
    public static Comment COMMENT311 = create(311L, 1023923311L, "Author Comment 311", "Comment 311", 3110L, new ArrayList<Long>(), false);
    public static Comment COMMENT312 = create(312L, 1023923312L, "Author Comment 312", "Comment 312", 3120L, new ArrayList<Long>(), false);
    public static Comment COMMENT321 = create(321L, 1023923321L, "Author Comment 321", "Comment 321", 3210L, new ArrayList<Long>(), false);

    public static Map<Long, Comment> MAP_COMMENT;

    public static String COMMENT1_JSON = "{\n"
            + "  \"by\" : \"Author Comment 1\",\n"
            + "  \"id\" : 1,\n"
            + "  \"kids\" : [ ],\n"
            + "  \"parent\" : 10,\n"
            + "  \"text\" : \"Comment 1\",\n"
            + "  \"time\" : 10239231,\n"
            + "  \"type\" : \"comment\"\n"
            + "}";

    public static String COMMENT_INVALID_JSON = "{\n"
            + "  \"id\" : 1,\n"
            + "  \"invalid\" : \"Invalid value\",\n"
            + "}";

    public static Comment create(long id, long timestamp, String author, String text,
                                 long parentId, List<Long> commentIds, boolean deleted) {
        Comment comment = Comment.newBuilder(id)
                .setTimestamp(timestamp)
                .setAuthor(author)
                .setText(text)
                .setParentId(parentId)
                .setCommentIds(commentIds)
                .setDeleted(deleted)
                .build();

        if (MAP_COMMENT == null) MAP_COMMENT = new HashMap<>();
        MAP_COMMENT.put(id, comment);

        return comment;
    }

    public static void resetDepth() {
        if (MAP_COMMENT != null) {
            for (Comment comment : MAP_COMMENT.values()) {
                comment.setDepth(0);
            }
        }
    }
}
