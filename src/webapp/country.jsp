<%@ taglib uri="webwork" prefix="ww" %>
<%@ taglib uri="benchmark" prefix="benchmark" %>

<benchmark:duration >
<ww:select label="multiple select test" name="select1" value="countries[0][1]" list="countries" listKey="[0]" listValue="[1]" multiple="true" size="5"/>
</benchmark:duration>

<table>
<ww:select label="multiple select test" name="select1" value="countries[0][1]" list="countries" listKey="[0]" listValue="[1]" multiple="true" size="5"/>
</table>

</table>

