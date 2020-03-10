package top.imuster.life.provider.service.impl;


import org.springframework.stereotype.Service;
import top.imuster.common.base.dao.BaseDao;
import top.imuster.common.base.service.BaseServiceImpl;
import top.imuster.life.api.pojo.ArticleReview;
import top.imuster.life.provider.dao.ArticleReviewDao;
import top.imuster.life.provider.service.ArticleReviewService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ArticleReviewService 实现类
 * @author 黄明人
 * @since 2020-01-30 15:25:20
 */
@Service("articleReviewService")
public class ArticleReviewServiceImpl extends BaseServiceImpl<ArticleReview, Long> implements ArticleReviewService {

    @Resource
    private ArticleReviewDao articleReviewDao;

    @Override
    public BaseDao<ArticleReview, Long> getDao() {
        return this.articleReviewDao;
    }

    @Override
    public List<ArticleReview> getFirstClassReviewInfoById(Long id) {
        ArticleReview condition = new ArticleReview();
        condition.setState(2);
        condition.setArticleId(id);
        List<ArticleReview> articleReviews = articleReviewDao.selectEntryList(condition);

        //查询每个一级留言信息下的回复总数
        ArticleReview review = new ArticleReview();
        review.setState(2);
        articleReviews.stream().forEach(articleReview -> {
            review.setParentId(articleReview.getParentId());
            Integer count = articleReviewDao.selectEntryListCount(review);
            articleReview.setChildTotalCount(count);
        });

        return articleReviews;
    }

    @Override
    public List<ArticleReview> reviewDetails(Long id) {
        ArticleReview articleReview = new ArticleReview();
        articleReview.setState(2);
        articleReview.setFirstClassId(id);
        List<ArticleReview> res = articleReviewDao.selectEntryList(articleReview);

        //将回复转换成一个map，这个map里面key为parentId，value为用户id
        Map<Long, Long> collect = res.stream().collect(Collectors.toMap(ArticleReview::getId, ArticleReview::getUserId));
        res.stream().forEach(condition -> {
            Long parentId = condition.getParentId();
            if(parentId != null){
                Long userId = collect.get(parentId);
                if(userId != null){
                    condition.setParentWriterId(userId);
                }
            }
        });
        return res;
    }

    @Override
    public List<ArticleReview> list(Long userId) {
        ArticleReview articleReview = new ArticleReview();
        articleReview.setUserId(userId);
        articleReview.setState(2);
        articleReview.setOrderField("create_time");
        articleReview.setOrderFieldType("DESC");
        articleReview.setFirstClassId(0l);
        articleReview.setParentId(0l);
        return articleReviewDao.selectEntryList(articleReview);
    }

    @Override
    public Long getUserIdByArticleReviewId(Long targetId) {
        return articleReviewDao.selectUserIdByReviewId(targetId);
    }

    @Override
    public List<ArticleReview> getUpTotalByIds(Long[] reviewIds) {
        return articleReviewDao.selectUpTotalByIds(reviewIds);
    }

    @Override
    public Long getUpTotal(Long id) {
        return articleReviewDao.selectUpTotalById(id);
    }
}