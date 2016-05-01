# DB Proj


## Setting

### DBConnection.java

개인 로컬 설정에 따라 포트, user, pwd를 바꿔줘야 합니다.


## 구조 설명

#### Control.java

기본적으로 UI 일을 하는 클래스로 유저의 상황에 맞춰 필요한 다른 클래스를 불러와 작동 시킵니다.

#### DBConnection.java

Tibero JDBC랑 connection 관리를 담당합니다.  
connection을 한번만 하기 위해 모든 다른 클래스들은 생성 시 인자로 DBConnection의 인스턴스를 받습니다.

`/* queries */` 아래에 필요한 쿼리 함수들을 만들어 사용하면 됩니다.

#### Login.java

로그인 역할을 담당합니다.  
login()은 리턴 시 안전히 로그인 되었음을 보장합니다.  
get 함수들로 name, id 값을 가져와 다른 class에서 받아 사용합니다.

#### Student.java

로그인한 유저가 student인 경우를 담당합니다.

#### Instructor.java

로그인한 유저가 instructor인 경우를 담당합니다.
