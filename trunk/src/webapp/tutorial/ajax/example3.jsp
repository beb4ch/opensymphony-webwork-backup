<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="webwork" prefix="ww" %>

<html>
    <head>
        <title>Ajax Examples</title>
            <jsp:include page="commonInclude.jsp" />
    </head>
  <body>

                    A simple DIV that obtains the update freq (3 secs) from the value stack/action:<br/>

                    <div class="code">
                        <pre>
                            &lt;ww:remotediv id="twoseconds" url="%{'/AjaxTest.action'}" updateFreq="%{'period'}" /&gt;
                        </pre>
                    </div>

                    <ww:remotediv id="twoseconds" url="%{'/AjaxTest.action'}" updateFreq="2" />

  </body>
</html>
