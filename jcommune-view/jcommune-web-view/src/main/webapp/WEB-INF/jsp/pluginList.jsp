<%--

    Copyright (C) 2011  JTalks.org Team
    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.
    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.
    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="jtalks" uri="http://www.jtalks.org/tags" %>

<head>
    <meta name="description" content="<c:out value="11"/>">
    <%-- Add JS plugins --%>
    <%--@elvariable id="isJsCompressed" type="java.lang.boolean"--%>
    <c:choose>
        <c:when test="${isJsCompressed}">
            <script type="text/javascript"
                    src="${pageContext.request.contextPath}/resources/wro/plugin.js?${project.version}"></script>
        </c:when>

        <c:otherwise>
            <script src="${pageContext.request.contextPath}/resources/javascript/app/utils.js"></script>
            <script src="${pageContext.request.contextPath}/resources/javascript/app/permissionService.js"></script>
        </c:otherwise>
    </c:choose>
</head>
<body>
    <div class="container">
        <%-- List of plugins. --%>
        <div id="plugins-list-header">
            <h2><spring:message code="label.plugins.installed"/></h2>
        </div>
        <span class="inline-block"></span>
        <%-- List of plugins. --%>
        <form:form action="${pageContext.request.contextPath}/plugins/update/activating" method="POST" modelAttribute="pluginsActivatingListDto">
            <table id="plugins-table" class="table table-row table-bordered">
                <c:choose>
                    <c:when test="${!(empty plugins)}">
                        <thead>
                        <tr>
                            <th id="plugin-name">
                                <spring:message code="label.plugins.plugin.name"/>
                            </th>
                            <th id="plugin-actions">
                                <spring:message code="label.plugins.plugin.actions"/>
                            </th>
                            <th id="plugin-is-enabled">
                                <spring:message code="label.plugins.plugin.is_enabled"/>
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="plugin" items="${plugins}" varStatus="status">
                                <%-- Plugin --%>
                                <tr>
                                    <td>
                                        <form:hidden path="activatingPlugins[${status.index}].pluginName" />
                                        <c:out value="${plugin.name}"/>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/plugins/configure/${plugin.name}"
                                           title="<spring:message code='label.plugins.plugin.configure.hint'/>">
                                           <spring:message code="label.plugins.plugin.configure"/>
                                        </a>
                                    </td>
                                    <td>
                                        <form:checkbox path="activatingPlugins[${status.index}].activated" checked="${plugin.state == 'ENABLED'}" />
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </c:when>
                </c:choose>
            </table>
            <input type="submit" value="<spring:message code="label.plugins.save"/>" />
        </form:form>
    </div>
</body>
