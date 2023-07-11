/*
 *  MIT Licence:
 *
 *  Copyright (C) 2018 Beanplanet Ltd
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without restriction
 *  including without limitation the rights to use, copy, modify, merge,
 *  publish, distribute, sublicense, and/or sell copies of the Software,
 *  and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 *  KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 *  PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 */

package org.beanplanet.testing.beans;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * A useful bean with every property type imaginable.
 *
 * @author Gary Watson
 */
public class TestBean {
   public static final String PUBLIC_STATIC_STRING = "PUBLIC_STATIC_STRING_VALUE";
   
   /** The primitive boolean property. */
   protected boolean primitiveBooleanProperty;
   
   /** The primitive byte property. */
   protected byte primitiveByteProperty;
   
   /** The primitive char property. */
   protected char primitiveCharProperty;
   
   /** The primitive double property. */
   protected double primitiveDoubleProperty;
   
   /** The primitive float property. */
   protected float primitiveFloatProperty;
   
   /** The primitive int property. */
   protected int primitiveIntProperty;
   
   /** The primitive long property. */
   protected long primitiveLongProperty;
   
   /** The primitive short property. */
   protected short primitiveShortProperty;   

   /** The boolean property. */
   protected Boolean booleanProperty;
   
   /** The byte property. */
   protected Byte byteProperty;
   
   /** The char property. */
   protected Character charProperty;
   
   /** The double property. */
   protected Double doubleProperty;
   
   /** The float property. */
   protected Float floatProperty;
   
   /** The int property. */
   protected Integer intProperty;
   
   /** The long property. */
   protected Long longProperty;
   
   /** The short property. */
   protected Short shortProperty;
   
   /** The string property. */
   protected String stringProperty;
   
   /** The date property. */
   protected Date dateProperty;
   
   /** The calendar property. */
   protected Calendar calendarProperty;
   
   /** The big decimal property. */
   protected BigDecimal bigDecimalProperty;
   
   /** The big integer property. */
   protected BigInteger bigIntegerProperty;
   
   /** The bean. */
   protected TestBean bean;
   
   /** The bean array. */
   protected TestBean beanArray[];
   
   /** The bean array2 dim. */
   protected TestBean beanArray2Dim[][];

   /** The bean array3 dim. */
   protected TestBean beanArray3Dim[][][];

   /** The bean collection. */
   protected Collection<TestBean> beanCollection;
   
   /** The bean collection2 dim. */
   protected Collection<Collection<TestBean>> beanCollection2Dim;
   
   /** The bean collection3 dim. */
   protected Collection<Collection<Collection<TestBean>>> beanCollection3Dim;
   
   /** The bean list. */
   protected List<TestBean> beanList;
   
   /** The bean list2 dim. */
   protected List<List<TestBean>> beanList2Dim;
   
   /** The bean list3 dim. */
   protected List<List<List<TestBean>>> beanList3Dim;
   
   /** Iterable property. */
   protected Iterable<?> objectIterable;

   /** Object list property. */
   protected List<?> objectList;

   /** Collection property. */
   protected Collection<?> collection;

   /**
    * Constructs an test been with no initial context.
    */
   public TestBean() {
   }
   
   /**
    * Constructs an test been with the specified string context.
    * 
    * @param stringProperty an initial string value.
    */
   public TestBean(String stringProperty) {
      setStringProperty(stringProperty);
   }
   
   /**
    * Constructs an test been with the specified string context.
    * 
    * @param stringProperty an initial string value.
    * @param intProperty an initial integer property.
    */
   public TestBean(String stringProperty, int intProperty) {
      setStringProperty(stringProperty);
      setIntProperty(intProperty);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      TestBean testBean = (TestBean) o;
      return primitiveBooleanProperty == testBean.primitiveBooleanProperty && primitiveByteProperty == testBean.primitiveByteProperty && primitiveCharProperty == testBean.primitiveCharProperty && Double.compare(testBean.primitiveDoubleProperty, primitiveDoubleProperty) == 0 && Float.compare(testBean.primitiveFloatProperty, primitiveFloatProperty) == 0 && primitiveIntProperty == testBean.primitiveIntProperty && primitiveLongProperty == testBean.primitiveLongProperty && primitiveShortProperty == testBean.primitiveShortProperty && Objects.equals(booleanProperty, testBean.booleanProperty) && Objects.equals(byteProperty, testBean.byteProperty) && Objects.equals(charProperty, testBean.charProperty) && Objects.equals(doubleProperty, testBean.doubleProperty) && Objects.equals(floatProperty, testBean.floatProperty) && Objects.equals(intProperty, testBean.intProperty) && Objects.equals(longProperty, testBean.longProperty) && Objects.equals(shortProperty, testBean.shortProperty) && Objects.equals(stringProperty, testBean.stringProperty) && Objects.equals(dateProperty, testBean.dateProperty) && Objects.equals(calendarProperty, testBean.calendarProperty) && Objects.equals(bigDecimalProperty, testBean.bigDecimalProperty) && Objects.equals(bigIntegerProperty, testBean.bigIntegerProperty) && Objects.equals(bean, testBean.bean) && Arrays.equals(beanArray, testBean.beanArray) && Arrays.equals(beanArray2Dim, testBean.beanArray2Dim) && Arrays.equals(beanArray3Dim, testBean.beanArray3Dim) && Objects.equals(beanCollection, testBean.beanCollection) && Objects.equals(beanCollection2Dim, testBean.beanCollection2Dim) && Objects.equals(beanCollection3Dim, testBean.beanCollection3Dim) && Objects.equals(beanList, testBean.beanList) && Objects.equals(beanList2Dim, testBean.beanList2Dim) && Objects.equals(beanList3Dim, testBean.beanList3Dim) && Objects.equals(objectIterable, testBean.objectIterable) && Objects.equals(objectList, testBean.objectList) && Objects.equals(collection, testBean.collection);
   }

   @Override
   public int hashCode() {
      int result = Objects.hash(primitiveBooleanProperty, primitiveByteProperty, primitiveCharProperty, primitiveDoubleProperty, primitiveFloatProperty, primitiveIntProperty, primitiveLongProperty, primitiveShortProperty, booleanProperty, byteProperty, charProperty, doubleProperty, floatProperty, intProperty, longProperty, shortProperty, stringProperty, dateProperty, calendarProperty, bigDecimalProperty, bigIntegerProperty, bean, beanCollection, beanCollection2Dim, beanCollection3Dim, beanList, beanList2Dim, beanList3Dim, objectIterable, objectList, collection);
      result = 31 * result + Arrays.hashCode(beanArray);
      result = 31 * result + Arrays.hashCode(beanArray2Dim);
      result = 31 * result + Arrays.hashCode(beanArray3Dim);
      return result;
   }

   /**
    * A property that is always null.
    * @return always null.
    */
   public Object getNull() {
      return null;
   }

   /**
    * Gets the primitive boolean property.
    * 
    * @return the primitiveBooleanProperty.
    */
   public boolean getPrimitiveBooleanProperty() {
      return primitiveBooleanProperty;
   }
   
   /**
    * Sets the primitive boolean property.
    * 
    * @param primitiveBooleanProperty
    *           the primitiveBooleanProperty to set.
    */
   public void setPrimitiveBooleanProperty(boolean primitiveBooleanProperty) {
      this.primitiveBooleanProperty = primitiveBooleanProperty;
   }
   
   /**
    * Gets the primitive byte property.
    * 
    * @return the primitiveByteProperty.
    */
   public byte getPrimitiveByteProperty() {
      return primitiveByteProperty;
   }
   
   /**
    * Sets the primitive byte property.
    * 
    * @param primitiveByteProperty
    *           the primitiveByteProperty to set.
    */
   public void setPrimitiveByteProperty(byte primitiveByteProperty) {
      this.primitiveByteProperty = primitiveByteProperty;
   }
   
   /**
    * Gets the primitive char property.
    * 
    * @return the primitiveCharProperty.
    */
   public char getPrimitiveCharProperty() {
      return primitiveCharProperty;
   }
   
   /**
    * Sets the primitive char property.
    * 
    * @param primitiveCharProperty
    *           the primitiveCharProperty to set
    */
   public void setPrimitiveCharProperty(char primitiveCharProperty) {
      this.primitiveCharProperty = primitiveCharProperty;
   }
   
   /**
    * Gets the primitive double property.
    * 
    * @return the primitiveDoubleProperty.
    */
   public double getPrimitiveDoubleProperty() {
      return primitiveDoubleProperty;
   }
   
   /**
    * Sets the primitive double property.
    * 
    * @param primitiveDoubleProperty
    *           the primitiveDoubleProperty to set.
    */
   public void setPrimitiveDoubleProperty(double primitiveDoubleProperty) {
      this.primitiveDoubleProperty = primitiveDoubleProperty;
   }
   
   /**
    * Gets the primitive float property.
    * 
    * @return the primitiveFloatProperty.
    */
   public float getPrimitiveFloatProperty() {
      return primitiveFloatProperty;
   }
   
   /**
    * Sets the primitive float property.
    * 
    * @param primitiveFloatProperty
    *           the primitiveFloatProperty to set
    */
   public void setPrimitiveFloatProperty(float primitiveFloatProperty) {
      this.primitiveFloatProperty = primitiveFloatProperty;
   }
   
   /**
    * Gets the primitive int property.
    * 
    * @return the primitiveIntProperty
    */
   public int getPrimitiveIntProperty() {
      return primitiveIntProperty;
   }
   
   /**
    * Sets the primitive int property.
    * 
    * @param primitiveIntProperty
    *           the primitiveIntProperty to set.
    */
   public void setPrimitiveIntProperty(int primitiveIntProperty) {
      this.primitiveIntProperty = primitiveIntProperty;
   }
   
   /**
    * Gets the primitive long property.
    * 
    * @return the primitiveLongProperty.
    */
   public long getPrimitiveLongProperty() {
      return primitiveLongProperty;
   }
   
   /**
    * Sets the primitive long property.
    * 
    * @param primitiveLongProperty
    *           the primitiveLongProperty to set.
    */
   public void setPrimitiveLongProperty(long primitiveLongProperty) {
      this.primitiveLongProperty = primitiveLongProperty;
   }
   
   /**
    * Gets the primitive short property.
    * 
    * @return the primitiveShortProperty.
    */
   public short getPrimitiveShortProperty() {
      return primitiveShortProperty;
   }
   
   /**
    * Sets the primitive short property.
    * 
    * @param primitiveShortProperty
    *           the primitiveShortProperty to set.
    */
   public void setPrimitiveShortProperty(short primitiveShortProperty) {
      this.primitiveShortProperty = primitiveShortProperty;
   }
   
   /**
    * Gets the boolean property.
    * 
    * @return the booleanProperty.
    */
   public Boolean getBooleanProperty() {
      return booleanProperty;
   }
   
   /**
    * Sets the boolean property.
    * 
    * @param booleanProperty
    *           the booleanProperty to set.
    */
   public void setBooleanProperty(Boolean booleanProperty) {
      this.booleanProperty = booleanProperty;
   }
   
   /**
    * Gets the byte property.
    * 
    * @return the byteProperty.
    */
   public Byte getByteProperty() {
      return byteProperty;
   }
   
   /**
    * Sets the byte property.
    * 
    * @param byteProperty
    *           the byteProperty to set.
    */
   public void setByteProperty(Byte byteProperty) {
      this.byteProperty = byteProperty;
   }
   
   /**
    * Gets the char property.
    * 
    * @return the charProperty.
    */
   public Character getCharProperty() {
      return charProperty;
   }
   
   /**
    * Sets the char property.
    * 
    * @param charProperty
    *           the charProperty to set.
    */
   public void setCharProperty(Character charProperty) {
      this.charProperty = charProperty;
   }
   
   /**
    * Gets the double property.
    * 
    * @return the doubleProperty.
    */
   public Double getDoubleProperty() {
      return doubleProperty;
   }
   
   /**
    * Sets the double property.
    * 
    * @param doubleProperty
    *           the doubleProperty to set.
    */
   public void setDoubleProperty(Double doubleProperty) {
      this.doubleProperty = doubleProperty;
   }
   
   /**
    * Gets the float property.
    * 
    * @return the floatProperty
    */
   public Float getFloatProperty() {
      return floatProperty;
   }
   
   /**
    * Sets the float property.
    * 
    * @param floatProperty
    *           the floatProperty to set
    */
   public void setFloatProperty(Float floatProperty) {
      this.floatProperty = floatProperty;
   }
   
   /**
    * Gets the int property.
    * 
    * @return the intProperty.
    */
   public Integer getIntProperty() {
      return intProperty;
   }
   
   /**
    * Sets the int property.
    * 
    * @param intProperty
    *           the intProperty to set.
    */
   public void setIntProperty(Integer intProperty) {
      this.intProperty = intProperty;
   }
   
   /**
    * Gets the long property.
    * 
    * @return the longProperty.
    */
   public Long getLongProperty() {
      return longProperty;
   }
   
   /**
    * Sets the long property.
    * 
    * @param longProperty
    *           the longProperty to set.
    */
   public void setLongProperty(Long longProperty) {
      this.longProperty = longProperty;
   }
   
   /**
    * Gets the short property.
    * 
    * @return the shortProperty
    */
   public Short getShortProperty() {
      return shortProperty;
   }
   
   /**
    * Sets the short property.
    * 
    * @param shortProperty
    *           the shortProperty to set
    */
   public void setShortProperty(Short shortProperty) {
      this.shortProperty = shortProperty;
   }
   
   /**
    * Gets the string property.
    * 
    * @return the stringProperty
    */
   public String getStringProperty() {
      return stringProperty;
   }
   
   /**
    * Sets the string property.
    * 
    * @param stringProperty
    *           the stringProperty to set.
    */
   public void setStringProperty(String stringProperty) {
      this.stringProperty = stringProperty;
   }
   
   /**
    * Gets the date property.
    * 
    * @return the dateProperty
    */
   public Date getDateProperty() {
      return dateProperty;
   }
   
   /**
    * Sets the date property.
    * 
    * @param dateProperty
    *           the dateProperty to set
    */
   public void setDateProperty(Date dateProperty) {
      this.dateProperty = dateProperty;
   }
   
   /**
    * Gets the calendar property.
    * 
    * @return the calendarProperty.
    */
   public Calendar getCalendarProperty() {
      return calendarProperty;
   }
   
   /**
    * Sets the calendar property.
    * 
    * @param calendarProperty
    *           the calendarProperty to set.
    */
   public void setCalendarProperty(Calendar calendarProperty) {
      this.calendarProperty = calendarProperty;
   }
   
   /**
    * Gets the big decimal property.
    * 
    * @return the bigDecimalProperty.
    */
   public BigDecimal getBigDecimalProperty() {
      return bigDecimalProperty;
   }
   
   /**
    * Sets the big decimal property.
    * 
    * @param bigDecimalProperty
    *           the bigDecimalProperty to set.
    */
   public void setBigDecimalProperty(BigDecimal bigDecimalProperty) {
      this.bigDecimalProperty = bigDecimalProperty;
   }
   
   /**
    * Gets the big integer property.
    * 
    * @return the bigIntegerProperty.
    */
   public BigInteger getBigIntegerProperty() {
      return bigIntegerProperty;
   }
   
   /**
    * Sets the big integer property.
    * 
    * @param bigIntegerProperty
    *           the bigIntegerProperty to set.
    */
   public void setBigIntegerProperty(BigInteger bigIntegerProperty) {
      this.bigIntegerProperty = bigIntegerProperty;
   }
   
   /**
    * Gets the bean.
    * 
    * @return the bean.
    */
   public TestBean getBean() {
      return bean;
   }
   
   /**
    * Sets the bean.
    * 
    * @param bean
    *           the new bean.
    */
   public void setBean(TestBean bean) {
      this.bean = bean;
   }

   /**
    * Gets the bean array.
    * 
    * @return the bean array
    */
   public TestBean[] getBeanArray() {
      return beanArray;
   }

   /**
    * Sets the bean array.
    * 
    * @param beanArray
    *           the new bean array.
    */
   public void setBeanArray(TestBean[] beanArray) {
      this.beanArray = beanArray;
   }

   /**
    * Gets the bean collection.
    * 
    * @return the bean collection.
    */
   public Collection<TestBean> getBeanCollection() {
      return beanCollection;
   }

   /**
    * Sets the bean collection.
    * 
    * @param beanCollection
    *           the new bean collection.
    */
   public void setBeanCollection(Collection<TestBean> beanCollection) {
      this.beanCollection = beanCollection;
   }

   /**
    * Gets the bean array2 dim.
    * 
    * @return the bean array2 dim.
    */
   public TestBean[][] getBeanArray2Dim() {
      return beanArray2Dim;
   }

   /**
    * Sets the bean array2 dim.
    * 
    * @param beanArray2Dim
    *           the new bean array2 dim.
    */
   public void setBeanArray2Dim(TestBean[][] beanArray2Dim) {
      this.beanArray2Dim = beanArray2Dim;
   }

   /**
    * Gets the bean array3 dim.
    * 
    * @return the bean array3 dim.
    */
   public TestBean[][][] getBeanArray3Dim() {
      return beanArray3Dim;
   }

   /**
    * Sets the bean array3 dim.
    * 
    * @param beanArray3Dim
    *           the new bean array3 dim.
    */
   public void setBeanArray3Dim(TestBean[][][] beanArray3Dim) {
      this.beanArray3Dim = beanArray3Dim;
   }

   /**
    * Gets the bean collection2 dim.
    * 
    * @return the bean collection2 dim.
    */
   public Collection<Collection<TestBean>> getBeanCollection2Dim() {
      return beanCollection2Dim;
   }

   /**
    * Sets the bean collection2 dim.
    * 
    * @param beanCollection2Dim
    *           the new bean collection2 dim
    */
   public void setBeanCollection2Dim(Collection<Collection<TestBean>> beanCollection2Dim) {
      this.beanCollection2Dim = beanCollection2Dim;
   }

   /**
    * Gets the bean collection3 dim.
    * 
    * @return the bean collection3 dim
    */
   public Collection<Collection<Collection<TestBean>>> getBeanCollection3Dim() {
      return beanCollection3Dim;
   }

   /**
    * Sets the bean collection3 dim.
    * 
    * @param beanCollection3Dim
    *           the new bean collection3 dim..
    */
   public void setBeanCollection3Dim(Collection<Collection<Collection<TestBean>>> beanCollection3Dim) {
      this.beanCollection3Dim = beanCollection3Dim;
   }

   /**
    * Gets the bean list.
    * 
    * @return the bean list.
    */
   public List<TestBean> getBeanList() {
      return beanList;
   }

   /**
    * Sets the bean list.
    * 
    * @param beanList
    *           the new bean list.
    */
   public void setBeanList(List<TestBean> beanList) {
      this.beanList = beanList;
   }

   /**
    * Gets the bean list2 dim.
    * 
    * @return the bean list2 dim.
    */
   public List<List<TestBean>> getBeanList2Dim() {
      return beanList2Dim;
   }

   /**
    * Sets the bean list2 dim.
    * 
    * @param beanList2Dim
    *           the new bean list2 dim.
    */
   public void setBeanList2Dim(List<List<TestBean>> beanList2Dim) {
      this.beanList2Dim = beanList2Dim;
   }

   /**
    * Gets the bean list3 dim.
    * 
    * @return the bean list3 dim.
    */
   public List<List<List<TestBean>>> getBeanList3Dim() {
      return beanList3Dim;
   }

   /**
    * Sets the bean list3 dim.
    * 
    * @param beanList3Dim
    *           the new bean list3 dim
    */
   public void setBeanList3Dim(List<List<List<TestBean>>> beanList3Dim) {
      this.beanList3Dim = beanList3Dim;
   }


   /**
    * Gets the iterable property.
    * 
    * @return the iterable property.
    */
   public Iterable<?> getObjectIterable() {
      return objectIterable;
   }

   /**
    * Sets the iterable property.
    * 
    * @param objectIterable
    *           the new iterable property.
    */
   public void setObjectIterable(Iterable<?> objectIterable) {
      this.objectIterable = objectIterable;
   }

   /**
    * Gets the object list property.
    * 
    * @return the objectList property or null if not set.
    */
   public List<?> getObjectList() {
      return objectList;
   }

   /**
    * Sets the object list property.
    * 
    * @param objectList the new objectList property.
    */
   public void setObjectList(List<?> objectList) {
      this.objectList = objectList;
   }

   /**
    * Gets the collection property.
    * 
    * @return the collection property or null if not set.
    */
   public Collection<?> getCollection() {
      return collection;
   }

   /**
    * Sets the collection property.
    * 
    * @param collection the collection to set.
    */
   public void setCollection(Collection<?> collection) {
      this.collection = collection;
   }
}
