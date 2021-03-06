package cn.wizzer.app.web.modules.tags;

import cn.wizzer.app.cms.modules.services.CmsArticleService;
import cn.wizzer.framework.page.Pagination;
import org.apache.commons.lang3.math.NumberUtils;
import org.beetl.core.GeneralVarTagBinding;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

/**
 * Created by wizzer on 2017/5/22.
 */
@IocBean
public class CmsArticleListTag extends GeneralVarTagBinding {
    @Inject
    private CmsArticleService cmsArticleService;

    @Override
    public void render() {
        String channelId = Strings.sNull(this.getAttributeValue("channelId"));
        int pageNumber = NumberUtils.toInt(Strings.sNull(this.getAttributeValue("pageNumber")));
        int pageSize = NumberUtils.toInt(Strings.sNull(this.getAttributeValue("pageSize")));
        Pagination pagination = cmsArticleService.getListPage(pageNumber, pageSize, Cnd.where("channelId", "=", channelId).and("disabled", "=", false).desc("publishAt"));
        this.binds(pagination);
        this.doBodyRender();
    }
}
