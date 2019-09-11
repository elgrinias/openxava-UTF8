/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/9.0.22
 * Generated at: 2019-09-11 08:43:13 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.xava.editors;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.openxava.model.meta.MetaProperty;
import org.openxava.util.Is;
import org.openxava.util.XavaException;
import org.openxava.web.editors.IUploadFilesIdsProvider;

public final class uploadEditor_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = new java.util.HashSet<>();
    _jspx_imports_classes.add("org.openxava.model.meta.MetaProperty");
    _jspx_imports_classes.add("org.openxava.util.XavaException");
    _jspx_imports_classes.add("org.openxava.util.Is");
    _jspx_imports_classes.add("org.openxava.web.editors.IUploadFilesIdsProvider");
  }

  private volatile javax.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public javax.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
      throws java.io.IOException, javax.servlet.ServletException {

    if (!javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
      final java.lang.String _jspx_method = request.getMethod();
      if ("OPTIONS".equals(_jspx_method)) {
        response.setHeader("Allow","GET, HEAD, POST, OPTIONS");
        return;
      }
      if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method)) {
        response.setHeader("Allow","GET, HEAD, POST, OPTIONS");
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET, POST or HEAD. Jasper also permits OPTIONS");
        return;
      }
    }

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write('\n');
      out.write('\n');
      out.write('\n');
      out.write('\n');
      out.write("  \n");
      out.write("\n");

String propertyKey = request.getParameter("propertyKey");
MetaProperty p = (MetaProperty) request.getAttribute(propertyKey);
String applicationName = request.getParameter("application");
String module = request.getParameter("module");
boolean editable = "true".equals(request.getParameter("editable"));
String dataEditable = editable?"":"data-editable='true'";
// tmp StringBuilder imagesOids = new StringBuilder(); 
Object value = request.getAttribute(propertyKey + ".value");
// tmp ini
String dataFiles = "";
String filesIds = null; 
if (!Is.empty(value)) {
	String filesIdsProviderClass = request.getParameter("filesIdsProviderClass");
	if (Is.emptyString(filesIdsProviderClass)) {
		throw new XavaException("filesIdsProviderClass parameter missed for uploadEditor.jsp"); // tmp i18n
	}
	IUploadFilesIdsProvider filesIdsProvider = (IUploadFilesIdsProvider) Class.forName(filesIdsProviderClass).newInstance(); 
	filesIds = filesIdsProvider.getFilesIds(value);
	if (filesIds != null) {
		dataFiles = "data-files='" + filesIds + "'"; 
	}
}
// tmp fin
// tmp String dataEmpty = imagesOids.length() == 0?"data-empty='true'":""; 
// tmp ini
System.out.println("[uploadEditor.jsp] 'null'.equals(value)=" + "null".equals(value));
String dataEmpty = "null".equals(value) || Is.empty(value) || !Is.empty(value) && filesIds != null && "".equals(filesIds)?"data-empty='true'":""; 
String cssClass = request.getParameter("cssClass");
cssClass = Is.emptyString(cssClass)?"":" " + cssClass;
boolean multiple = "true".equals(request.getParameter("multipleFiles"));
String dataMultiple = multiple?"data-multiple='true'":"";
boolean preview = !"false".equals(request.getParameter("imagePreview"));
String dataPreview = !preview?"data-preview='false'":"";
// tmp fin

      out.write("\n");
      out.write("<input id='");
      out.print(propertyKey);
      out.write("' \n");
      out.write("\ttype=\"file\" class=\"xava_upload");
      out.print(cssClass);
      out.write("\"\n");
      out.write("\tdata-application=\"");
      out.print(applicationName);
      out.write("\" \n");
      out.write("\tdata-module=\"");
      out.print(module);
      out.write('"');
      out.write('\n');
      out.write('	');
      out.print(dataMultiple);
      out.write('\n');
      out.write('	');
      out.print(dataPreview);
      out.write('\n');
      out.write('	');
      out.print(dataFiles);
      out.write(' ');
      out.write('\n');
      out.write('	');
      out.print(dataEmpty);
      out.write('\n');
      out.write('	');
      out.print(dataEditable);
      out.write("/>\n");
      out.write("\n");
      out.write("<input type=\"hidden\" name=\"");
      out.print(propertyKey);
      out.write("\" value=\"");
      out.print(value);
      out.write("\">\n");
      out.write("\n");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "filePondTranslation.jsp", out, false);
      out.write('	');
      out.write('\n');
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
