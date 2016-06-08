package com.github.trentonadams.eve.app.model;

/**
 * This is just a way of making it so that each JAX-RS service can return the
 * same base functionality, such as what page we're accessing.  This allows us
 * to use "${model.page}".  See index.jsp for examples.
 * <p/>
 * Created :  08/03/16 8:53 PM MST
 * <p/>
 * Modified : $Date$ UTC
 * <p/>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public interface PageModel
{
    String getPage();

    void setPage(String page);
}
