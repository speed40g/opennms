<%@page language="java" contentType="text/html" session="true" import="org.opennms.web.response.*,java.util.*,org.opennms.web.*" %>

<%  
    Calendar now = Calendar.getInstance();

    Calendar yesterday = Calendar.getInstance();
    yesterday.add( Calendar.DATE, -1 );
%>

<html>
<head>
  <title>Custom | Response Time | Reports | OpenNMS Web Console</title>
  <base HREF="<%=org.opennms.web.Util.calculateUrlBase( request )%>" />
  <link rel="stylesheet" type="text/css" href="includes/styles.css" />
</head>
<body marginwidth="0" marginheight="0" LEFTMARGIN="0" RIGHTMARGIN="0" TOPMARGIN="0">

<% String breadcrumb1 = java.net.URLEncoder.encode("<a href='report/index.jsp'>Reports</a>"); %>
<% String breadcrumb2 = java.net.URLEncoder.encode("<a href='response/index.jsp'>Response Time</a>"); %>
<% String breadcrumb3 = java.net.URLEncoder.encode("Custom"); %>
<jsp:include page="/includes/header.jsp" flush="false" >
  <jsp:param name="title" value="Custom Response Time Reporting" />
  <jsp:param name="breadcrumb" value="<%=breadcrumb1%>" />
  <jsp:param name="breadcrumb" value="<%=breadcrumb2%>" />
  <jsp:param name="breadcrumb" value="<%=breadcrumb3%>" />
</jsp:include>

<br>
<!-- Body -->

<table width="100%" cellspacing="0" cellpadding="0" border="0">
  <tr>
    <td> &nbsp; </td>

    <td>
    <form METHOD="GET" ACTION="response/adhoc4.jsp" >
      <%=Util.makeHiddenTags( request )%>

      <table width="100%" cellspacing="2" cellpadding="2" border="0">

        <tr>
          <td>
            <h3>Step 3: Choose the Title for the Graph</h3>
          </td>
        </tr>

        <tr>
          <td>
            Title: <input type="text" name="title" value="Graph Title" />
          </td>
        </tr>

        <tr><td height="20">&nbsp;</td></tr>

        <tr>
          <td>
            <h3>Step 4: Choose the Time Span of the Graph</h3>
          </td>
        </tr>

        <tr>
          <td valign="top">
            Query from date<br>

            <select name="startMonth" size="1">
              <% for( int i = 0; i < 12; i++ ) { %>
                 <option value="<%=i%>" <% if( yesterday.get( Calendar.MONTH ) == i ) out.print("selected=\"selected\" ");%>><%=MONTHS[i]%></option>
              <% } %>
            </select>

            <input type="text" name="startDate" size="4" maxlength="2" value="<%=yesterday.get( Calendar.DATE )%>" />
            <input type="text" name="startYear" size="6" maxlength="4" value="<%=yesterday.get( Calendar.YEAR )%>" />

            <select name="startHour" size="1">
              <%
                 int yesterdayHour = yesterday.get( Calendar.HOUR_OF_DAY );
                 for( int i = 1; i < 25; i++ ) {
              %>
                <option value="<%=i%>" <% if( yesterdayHour == i ) out.print( "selected " ); %>>
                  <%=(i<13) ? i : i-12%>&nbsp;<%=(i<13) ? "AM" : "PM"%>
                </option>
              <% } %>
            </select>

          </td>
        </tr>

        <tr>
          <td valign="top">
            Query to date<br>

            <select name="endMonth" size="1">
              <% for( int i = 0; i < 12; i++ ) { %>
                 <option value="<%=i%>" <% if( now.get( Calendar.MONTH ) == i ) out.print("selected=\"selected\" ");%>><%=MONTHS[i]%></option>
              <% } %>
            </select>

            <input type="text" name="endDate" size="4" maxlength="2" value="<%=now.get( Calendar.DATE )%>" />
            <input type="text" name="endYear" size="6" maxlength="4" value="<%=now.get( Calendar.YEAR )%>" />

            <select name="endHour" size="1">
              <%
                 int nowHour = now.get( Calendar.HOUR_OF_DAY );
                 for( int i = 1; i < 25; i++ ) {
              %>
                <option value="<%=i%>" <% if( nowHour == i ) out.print( "selected " ); %>>
                  <%=(i<13) ? i : i-12%>&nbsp;<%=(i<13) ? "AM" : "PM"%>
                </option>
              <% } %>
            </select>

          </td>
        </tr>

        <tr><td>&nbsp;</td></tr>

        <tr>
          <td>
            <input type="submit" value="Next"/>
            <input type="reset" />
          </td>
        </tr>
      </table>
    </form>
    </td>

    <td> &nbsp; </td>
  </tr>
</table>

<br>

<jsp:include page="/includes/footer.jsp" flush="false" />

</body>
</html>

<%!
    //note these run from 0-11, this is because of java.util.Calendar!
    public static final String[] MONTHS = new String[] {
       "January",
       "February",
       "March",
       "April",
       "May",
       "June",
       "July",
       "August",
       "September",
       "October",
       "November",
       "December"
    };
%>
