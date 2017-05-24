package cn.wizzer.app.web.modules.controllers.platform.shop;

import cn.wizzer.framework.base.Result;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import cn.wizzer.framework.util.StringUtil;
import cn.wizzer.app.shop.modules.models.Shop_shipping;
import cn.wizzer.app.shop.modules.services.ShopShippingService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@IocBean
@At("/platform/shop/shipping")
public class ShopShippingController{
    private static final Log log = Logs.get();
    @Inject
    private ShopShippingService shopShippingService;

    @At("")
    @Ok("beetl:/platform/shop/shipping/index.html")
    @RequiresPermissions("shop.logistics.shipping")
    public void index() {
    }

    @At("/data")
    @Ok("json")
    @RequiresPermissions("shop.logistics.shipping")
    public Object data(@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
    	return shopShippingService.data(length, start, draw, order, columns, cnd, null);
    }

    @At("/add")
    @Ok("beetl:/platform/shop/shipping/add.html")
    @RequiresPermissions("shop.logistics.shipping")
    public void add() {

    }

    @At("/addDo")
    @Ok("json")
    @RequiresPermissions("shop.logistics.shipping.add")
    @SLog(tag = "Shop_shipping", msg = "${args[0].id}")
    public Object addDo(@Param("..")Shop_shipping shopShipping, HttpServletRequest req) {
		try {
			shopShippingService.insert(shopShipping);
			return Result.success("system.success");
		} catch (Exception e) {
			return Result.error("system.error");
		}
    }

    @At("/edit/?")
    @Ok("beetl:/platform/shop/shipping/edit.html")
    @RequiresPermissions("shop.logistics.shipping")
    public void edit(String id,HttpServletRequest req) {
		req.setAttribute("obj", shopShippingService.fetch(id));
    }

    @At("/editDo")
    @Ok("json")
    @RequiresPermissions("shop.logistics.shipping.edit")
    @SLog(tag = "Shop_shipping", msg = "${args[0].id}")
    public Object editDo(@Param("..")Shop_shipping shopShipping, HttpServletRequest req) {
		try {
            shopShipping.setOpBy(StringUtil.getUid());
			shopShipping.setOpAt((int) (System.currentTimeMillis() / 1000));
			shopShippingService.updateIgnoreNull(shopShipping);
			return Result.success("system.success");
		} catch (Exception e) {
			return Result.error("system.error");
		}
    }

    @At({"/delete/?", "/delete"})
    @Ok("json")
    @RequiresPermissions("shop.logistics.shipping.delete")
    @SLog(tag = "Shop_shipping", msg = "${req.getAttribute('id')}")
    public Object delete(String id, @Param("ids")  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				shopShippingService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				shopShippingService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @At("/detail/?")
    @Ok("beetl:/platform/shop/shipping/detail.html")
    @RequiresPermissions("shop.logistics.shipping")
	public void detail(String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", shopShippingService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
    }

}
