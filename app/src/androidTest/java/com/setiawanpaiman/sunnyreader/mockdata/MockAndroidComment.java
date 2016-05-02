package com.setiawanpaiman.sunnyreader.mockdata;

import com.setiawanpaiman.sunnyreader.data.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class MockAndroidComment {

    public static Comment create(long id, long timestamp, String author, String text,
                                 long parentId, List<Long> commentIds, boolean deleted,
                                 int totalReplies, int depth) {
        Comment comment = Comment.newBuilder(id)
                .setTimestamp(timestamp)
                .setAuthor(author)
                .setText(text)
                .setParentId(parentId)
                .setCommentIds(commentIds)
                .setDeleted(deleted)
                .build();
        comment.setTotalReplies(totalReplies);
        comment.setDepth(depth);
        return comment;
    }

    public static Comment generateMockComment(long id, int totalReplies, int depth) {
        return create(id, 1462133885L - (id * 1000), "Author Comment " + id,
                "\n\n<b>Comment</b> " + id + "\n\n", id,
                new ArrayList<Long>(), false, totalReplies, depth);
    }

    public static List<Comment> generateMockComments(int start, int count, int maxDepth) {
        List<Comment> comments = new ArrayList<>();
        for (int i = start; i < start + count; i++) {
            comments.addAll(generateMockComments(i, i + 1, 0, maxDepth));
        }
        return comments;
    }

    private static List<Comment> generateMockComments(long id, int totalReplies, int depth, int maxDepth) {
        if (depth > maxDepth) return new ArrayList<>();
        else {
            List<Comment> comments = new ArrayList<>();
            comments.add(generateMockComment(id, depth == maxDepth ? 0 : totalReplies, depth));
            for (int i = 1; i <= totalReplies; i++) {
                long newId = (id * 10) + i;
                comments.addAll(generateMockComments(newId, totalReplies + 1, depth + 1, maxDepth));
            }
            return comments;
        }
    }
}
