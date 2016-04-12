package com.github.trentonadams.eve.features;

import com.github.trentonadams.eve.MainView;
import com.github.trentonadams.eve.PageModel;
import org.glassfish.jersey.server.mvc.Template;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Created :  08/03/16 4:12 PM MST
 * <p/>
 * Modified : $Date$ UTC
 * <p/>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
@Path("/api-keys")
public class ApiKeys extends PageModel
{
    private static final String SAMPLE_JSP =
        "/WEB-INF/jsp/com/github/trentonadams/eve/features/ApiKeys/sample.jsp";
    private static final String MAIN_JSP =
        "/WEB-INF/jsp/com/github/trentonadams/eve/features/ApiKeys/main.jsp";

    /**
     * The JSP page to access
     */
    private String page;

    /**
     * The default handler for the ApiKeys.
     *
     * @return this service referencing {@link ApiKeys}
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Template(name = MainView.INDEX_JSP)
    public ApiKeys getService()
    {
        page = MAIN_JSP;
        return this;
    }

    @Path("sample")
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Template(name = MainView.INDEX_JSP)
    public ApiKeys getSample()
    {
        page = SAMPLE_JSP;
        return this;
    }


    /**
     * @return the JSP page being used for the current operation.
     */
    public String getPage()
    {
        return page;
    }
}
