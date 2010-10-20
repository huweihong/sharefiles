import java.text.SimpleDateFormat
class GroovyObjectNotation {
    private Class preferredMapClass = null;
    private Class preferredSetClass = LinkedHashSet.class

    private String dateFormatText = "yyyyMMdd-HH:mm:ss.SSSSSS Z"
    private SimpleDateFormat dtFrmt = new SimpleDateFormat(dateFormatText)

    public writeList(list) { return "[ " + list.collect{ t -> writeObj(t) }.join(", ") + " ]" }

    public writeSet(Set set) {  return ("new ${preferredSetClass.getName()}( ${writeList(set)} )").toString(); }

    public writeMap( Map map) {
        def body = '[' + map.collect { k, v -> "${writeObj(k)} : ${writeObj(v)}" }.join(", ") + ']'
        if (preferredMapClass==null) return body;
        return ('new ' + preferredMapClass.getName() + "( " + body + " )")
    }

    public writeNumber(Number obj) {
        if (obj instanceof Double) return "${obj}d";
        if (obj instanceof Float) return "${obj}f";
        if (obj instanceof Integer) return "${obj}i";
        if (obj instanceof Long) return "${obj}L";
        if (obj instanceof BigInteger) return "${obj}g"; // not really a fan of this particular language notation
        if (obj instanceof BigDecimal) return "${obj}g"; // not really a fan of this particular language notation

        if (obj instanceof Short) return "${obj} as short";
        if (obj instanceof Byte) return "${obj} as byte";
        return obj;
    }

    public String writeDate( obj ) {
        if (obj instanceof java.sql.Timestamp) return 'new java.sql.Timestamp(Date.parse(\'' + dateFormatText + '\',\''+dtFrmt.format(obj)+'\').getTime())'
        else if (obj instanceof java.sql.Time) return 'new java.sql.Time(Date.parse(\'' + dateFormatText + '\',\''+dtFrmt.format(obj)+'\').getTime())'
        else if (obj instanceof java.sql.Date) return 'new java.sql.Date(Date.parse(\'' + dateFormatText + '\',\''+dtFrmt.format(obj)+'\').getTime())'
        else if (obj instanceof java.util.Date) return 'Date.parse(\'' + dateFormatText + '\',\''+dtFrmt.format(obj)+'\')'
        else if (obj instanceof java.util.Calendar) return 'new GregorianCalendar(Date.parse(\'' + dateFormatText + '\',\''+dtFrmt.format(obj)+'\').getTime())'
        else return obj.toString()
    }

    public writeObj(obj) {
        Object.metaClass.toGroovyCode={-> delegate.toString() }

        if(obj == null) return 'null'
        else if (obj instanceof Set) return writeSet(obj)
        else if (obj instanceof List) return writeList(obj)
        else if (obj instanceof Map) return writeMap(obj)
        else if (obj.getClass().isArray()) return writeList(Arrays.asList(obj)) + ' as ' + obj.getClass().getSimpleName()
        else if (obj instanceof Date || obj instanceof Calendar) return writeDate(obj)
        else if (obj instanceof Number) return writeNumber(obj)
        else if (obj instanceof Boolean) return obj ? true : false

        def retVal = obj.toGroovyCode() == obj.toString() ?
            "'" + obj.toString().replaceAll('([^\\\\])\'','$1\\\\\'') + "'" :
            obj.toGroovyCode()

        return retVal
    }
}
