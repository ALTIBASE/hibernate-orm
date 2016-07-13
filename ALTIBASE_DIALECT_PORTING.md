# AltibaseDialect Ŭ���� ���� ���� ���
�� ���������� �������� AltibaseDialect.java�� �������Ͽ� ������ hibernate jar���Ͽ� ��ġ�� ����� �����Ѵ�.
hibernate core ���̺귯���� ��°�� �����ϴ� ����� [�̰�](README.md)�� �����ϱ� �ٶ���.

## Hibernate core ���̺귯�� �ٿ�ε�
Hibernate ���� ��������Ʈ���� �� ������ core ���̺귯���� �ٿ�ε��Ѵ�.

* [http://hibernate.org/orm/downloads/](http://hibernate.org/orm/downloads/) ���� �ٿ�ε尡 �����ϸ� ���� �ٿ�ε� ������ ������ �Ʒ��� ����.
    * 5.2.1
    * 5.1.0
    * 5.0.9
    * 4.3.11
    * 4.2.21


* ���� ��ũ�Ǿ� �ִ� ���̺귯������ AltibaseDialect.class �� ���ԵǾ� ���� �ʱ⶧���� �������� AltibaseDialect.java�� �������ؼ� �־���� �Ѵ�.

## AltibaseDialect.java ������
Hibernate ������ AltibaseDialect �ҽ��� �Ʒ� URL���� �ٿ�ε� �� �� �ִ�. ���� Hibernate 4.3 �������ʹ� AltibaseDialect.java�� ���Ҿ� AltibaseLimitHandler.java�� �߰��Ǿ��� ������ �Բ� �������ؾ� �Ѵ�.(jdk1.6�̻� �ʿ�)

| Hibernate Ver  |AltibaseDialect.java | AltibaseLimitHandler.java | 
|---|---|---|
|3.6|https://github.com/ALTIBASE/hibernate-orm/blob/3.6/hibernate-core/src/main/java/org/hibernate/dialect/AltibaseDialect.java |   |
|4.2|https://github.com/ALTIBASE/hibernate-orm/blob/4.2/hibernate-core/src/main/java/org/hibernate/dialect/AltibaseDialect.java |   |
|4.3|https://github.com/ALTIBASE/hibernate-orm/blob/4.3/hibernate-core/src/main/java/org/hibernate/dialect/AltibaseDialect.java | https://github.com/ALTIBASE/hibernate-orm/blob/4.3/hibernate-core/src/main/java/org/hibernate/dialect/pagination/AltibaseLimitHandler.java   |
|5.0|https://github.com/ALTIBASE/hibernate-orm/blob/5.0/hibernate-core/src/main/java/org/hibernate/dialect/AltibaseDialect.java | https://github.com/ALTIBASE/hibernate-orm/blob/5.0/hibernate-core/src/main/java/org/hibernate/dialect/pagination/AltibaseLimitHandler.java   |
|5.1|https://github.com/ALTIBASE/hibernate-orm/blob/5.1/hibernate-core/src/main/java/org/hibernate/dialect/AltibaseDialect.java | https://github.com/ALTIBASE/hibernate-orm/blob/5.1/hibernate-core/src/main/java/org/hibernate/dialect/pagination/AltibaseLimitHandler.java   |

1. hibernate jar ��������
    �ٿ�ε��� Hibernate ���̺귯�� ���� �� hibernate-core-x.x.x.Final.jar ������ �ٿ�ε��� �ҽ���ġ�� �̵���Ű�� ���������Ѵ�.
    `mv hibernate-core-x.x.x.Final.jar �ٿ�ε��� �ҽ� ���丮`
    `cd �ٿ�ε��� �ҽ� ���丮`
    `jar xvf hibernate-core-x.x.x.Final.jar`
2. AltibaseLimitHandler.java �� AltibaseDialect.java ������
    `javac -d . -cp . AltibaseLimitHandler.java`
    `javac -d . -cp . AltibaseDialect.java`
3. Compile�� ���������� �Ǹ� ������丮�� ������ ���� �ΰ��� Ŭ���������� �߰��ȴ�.
    `./org/hibernate/dialect/AltibaseDialect.class`
    `./org/hibernate/dialect/pagination/AltibaseLimitHandler.class`

## AltibaseDialect Ŭ������ Hibernate jar���Ͽ� ����
jar ����� ���� ���� �������� AltibaseDialect ���� Ŭ�������� jar�� ���´�.
    `rm AltibaseLimitHandler.java AltibaseDialect.java`
    `jar -cvfm hibernate-core-x.x.x.Final.jar META-INF/MANIFEST.MF .`
