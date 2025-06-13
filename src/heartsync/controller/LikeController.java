package heartsync.controller;

import java.util.List;

import heartsync.dao.LikeDAO;
import heartsync.model.User;

public class LikeController {
    private LikeDAO likeDAO;

    public LikeController() {
        this.likeDAO = new LikeDAO();
    }

    public boolean addLike(int likerId, int likedUserId) {
        return likeDAO.addLike(likerId, likedUserId);
    }

    public boolean removeLike(int likerId, int likedUserId) {
        return likeDAO.removeLike(likerId, likedUserId);
    }

    public List<User> getLikers(int userId) {
        return likeDAO.getLikers(userId);
    }

    public boolean hasLiked(int likerId, int likedUserId) {
        return likeDAO.hasLiked(likerId, likedUserId);
    }
}
