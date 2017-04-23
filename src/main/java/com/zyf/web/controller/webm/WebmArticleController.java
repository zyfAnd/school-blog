package com.zyf.web.controller.webm;

import com.zyf.basic.utils.BaseSearch;
import com.zyf.basic.utils.PageableUtil;
import com.zyf.basic.utils.ParamFilterUtil;
import com.zyf.basic.utils.SearchDto;
import com.zyf.web.model.Account;
import com.zyf.web.model.Article;
import com.zyf.web.service.IArticleService;
import com.zyf.web.service.ICategoryService;
import com.zyf.web.tools.EmailTools;
import com.zyf.web.tools.RequestTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by zhang
 */
@Controller
@RequestMapping(value = "webm/article")
public class WebmArticleController {

    Logger logger = LoggerFactory.getLogger("WebmArticleController");
    @Autowired
    private IArticleService articleService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private EmailTools emailTools;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(Model model, Integer page, HttpServletRequest request) {
        Account a = RequestTools.getAccount(request);
        Specifications spes = Specifications.where(new BaseSearch<Article>(new SearchDto("accountId", "eq", a.getId())));
        Page<Article> datas = articleService.findAll(new ParamFilterUtil<Article>().buildSearch(model, request, spes),
                PageableUtil.basicPage(page));
        model.addAttribute("datas", datas);

        return "webm/article/list";
    }

    @RequestMapping(value = "add")
    public String add(Model model, Article article, HttpServletRequest request) {
        Account a = RequestTools.getAccount(request);
        String method = request.getMethod();
        if("get".equalsIgnoreCase(method)) {
            article = article == null?new Article():article;
            model.addAttribute("article", article);
            model.addAttribute("cateList", categoryService.findAll(
                    Specifications.where(new BaseSearch<>(new SearchDto("accountId", "eq", a.getId())))));
            return "webm/article/add";
        } else {
            article.setAccountEmail(a.getEmail());
            article.setAccountId(a.getId());
            article.setAccountName(a.getNickname());
            article.setStatus("1");
            article.setCommentCount(0);
            article.setCreateDate(new Date());
            article.setIsGood(0);
            article.setIsShow(1);
            article.setReadCount(0);

            articleService.save(article);
            emailTools.sendOnAddArticle(RequestTools.getAdminEmail(request), article.getTitle(),
                    article.getContent(), RequestTools.getCurUrl(request)+"/web/article/detail/"+article.getId());
            return "redirect:/webm/article/list";
        }
    }

    @RequestMapping(value = "update")
    public String update(Model model, Integer id, Article article, HttpServletRequest request) {
        Account a = RequestTools.getAccount(request);
        String method = request.getMethod();
        if("get".equalsIgnoreCase(method)) {
            logger.info("get method "+"----------");
            Article art = articleService.findByIdAndAccountId(id, a.getId());
            model.addAttribute("article", art);
            model.addAttribute("cateList", categoryService.findAll(
                    Specifications.where(new BaseSearch<>(new SearchDto("accountId", "eq", a.getId())))));
            return "webm/article/update";
        } else {
            logger.info("post method "+"----------");
            Article art = articleService.findByIdAndAccountId(id, a.getId());
            if(art!=null) {
                art.setTitle(article.getTitle());
                art.setContent(article.getContent());
                art.setCateName(article.getCateName());
                art.setCateId(article.getCateId());
                articleService.save(art);

                emailTools.sendOnAddArticle(RequestTools.getAdminEmail(request), article.getTitle(),
                        article.getContent(), RequestTools.getCurUrl(request)+"/web/article/detail/"+art.getId());
            }
            return "redirect:/webm/article/list";
        }
    }
}
