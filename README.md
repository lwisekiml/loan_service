## Ch02. 대출 상담 기능 개발
### 02. 대출 상담 도메인 테이블 정의
코드 실행하면 아래 쿼리가 적용되어 테이블이 생성된다.

    create table counsel (
        counsel_id bigint generated by default as identity,
        created_at datetime default CURRENT_TIMESTAMP NOT NULL COMMENT '생성일자',
        is_deleted bit default false NOT NULL COMMENT '이용가능여부',
        updated_at datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '수정일자',
        address varchar(50) DEFAULT NULL COMMENT '주소',
        address_detail varchar(50) DEFAULT NULL COMMENT '주소 상세',
        applied_at datetime DEFAULT NULL COMMENT '신청일자',
        cell_phone varchar(13) DEFAULT NULL COMMENT '전화번호',
        email varchar(50) DEFAULT NULL COMMENT '책임자 이메일',
        memo text DEFAULT NULL COMMENT '상담 메모',
        name varchar(12) DEFAULT NULL COMMENT '상담 요청자',
        zip_code varchar(5) DEFAULT NULL COMMENT '우편번호',
        primary key (counsel_id)
    )

<br/>

### 03. 대출 상담 등록 기능 구현
- postman  


    <요청>
    [POST] http://localhost:8080/counsels  
    [json]
    {
        "name":"박아무",
        "cellPhone":"010-1234-5678",
        "email":"email@loan.com",
        "memo":"대출 상담을 원함",
        "address":"서울 아무곳",
        "addressDetail":"123-45",
        "zipCode":"11122"
    }

    <응답>
    {
        "result": {
            "code": "0000",
            "desc": "success"
        },
        "data": {
            "counselId": 1,
            "name": "박아무",
            "cellPhone": "010-1234-5678",
            "email": "email@loan.com",
            "memo": "대출 상담을 원함",
            "address": "서울 아무곳",
            "addressDetail": "123-45",
            "zipCode": "11122",
            "appliedAt": "2024-02-23T09:12:50.2105476",
            "createdAt": "2024-02-23T09:12:50.2355479",
            "updatedAt": "2024-02-23T09:12:50.2355479"
        }
    }

<br/>

### 04.대출 상담 조회 기능 구현
- postman


    <요청>
    [GET] http://localhost:8080/counsels/1

    <응답>
    {
        "result": {
            "code": "0000",
            "desc": "success"
        },
        "data": {
            "counselId": 1,
            "name": "박아무",
            "cellPhone": "010-1234-5678",
            "email": "email@loan.com",
            "memo": "대출 상담을 원함",
            "address": "서울 아무곳",
            "addressDetail": "123-45",
            "zipCode": "11122",
            "appliedAt": "2024-02-23T09:12:50.210548",
            "createdAt": "2024-02-23T09:12:50.235548",
            "updatedAt": "2024-02-23T09:12:50.235548"
        }
    }

    <요청>
    [GET] http://localhost:8080/counsels/2

    <응답>
    {
        "result": {
            "code": "9000",
            "desc": "system error"
        },
        "data": null
    }

<br/>

### 05.대출 상담 수정 기능 구현
- postman


    <요청>
    [PUT] http://localhost:8080/counsels/1
    [json]
    {
        "name":"김아무",
        "cellPhone":"010-5678-1234",
        "email":"email@loan.com",
        "memo":"대출 상담을 원함",
        "address":"수원 아무곳",
        "addressDetail":"123-45",
        "zipCode":"22211"
    }

    <응답>
    {
        "result": {
            "code": "0000",
            "desc": "success"
        },
        "data": {
            "counselId": 1,
            "name": "김아무",
            "cellPhone": "010-5678-1234",
            "email": "email@loan.com",
            "memo": "대출 상담을 원함",
            "address": "수원 아무곳",
            "addressDetail": "123-45",
            "zipCode": "22211",
            "appliedAt": "2024-02-23T09:42:47.275373",
            "createdAt": "2024-02-23T09:42:47.299372",
            "updatedAt": "2024-02-23T09:43:11.5815086"
        }
    }

<br/>

### 06.대출 상담 삭제 기능 구현
- postman


    <요청>
    [DELETE] http://localhost:8080/counsels/1
    
    <응답>
    {
        "result": {
            "code": "0000",
            "desc": "success"
        },
        "data": null
    }

## Ch03. 대출 신청 기능 개발
