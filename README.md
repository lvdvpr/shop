> **프로젝트 소개**

상품 관리 / 주문관리 / 댓글기능 등을 구현한 쇼핑몰입니다.

> **사용기술**

- FrontEnd : HTML, CSS, JavaScript, Thymeleaf
- BackEnd : Java, SpringBoot, JPA, python, AWS S3
- DataBase :  Azure, MySQL

> **개발환경**
 
- IDE : IntelliJ
- Version Control : Git/Github
- Build Tool : Gradle

> **기능 구현**

- Azure을 활용하여 MySQL을 호스팅
- python 크롤링으로 수집한 데이터를 데이터베이스에 적재하였습니다.
- [AWS S3](https://github.com/lvdvpr/shop/blob/main/src/main/java/com/apple/shop/item/S3Service.java)를 활용한 이미지 업로드기능 구현
- [@ManyToOne](https://github.com/lvdvpr/shop/blob/main/src/main/java/com/apple/shop/sales/Sales.java) 사용시 N+1 방지를 위한 [JOIN FETCH](https://github.com/lvdvpr/shop/blob/main/src/main/java/com/apple/shop/sales/SalesRepository.java) 적용
- Spring Security를 적용한 가입 및 로그인기능
- [full text index](https://github.com/lvdvpr/shop/blob/main/src/main/java/com/apple/shop/item/ItemRepository.java#L16-L17)를 활용하여 검색 기능 성능 개선
- 그밖에 페이지기능, 댓글 기능, 주문기능, 글 발행/수정/삭제기능 등을 구현

> **문제 해결**

- 문제 : full text index를 활용할 경우 한글자로는 검색이 안되는 문제 발생
- 해결 : 사용자가 입력한 검색어가 한 글자일 경우에는 Like문을 활용하여 검색하고, 두 글자 이상일 경우 full text index를 활용하여 검색되도록 구현
