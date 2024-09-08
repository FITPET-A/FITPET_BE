# FITPET_BE
대학생 IT경영학회 큐시즘 30기 기업 프로젝트 : FITPET A조 Backend Repository <br>
2024.08.19 ~ 2024.09.08

![1](https://github.com/user-attachments/assets/0f660c6e-7207-4f7f-b101-f687bd0e12bc) 
<br><br><br>




## Member
|      김다은       |          김수진        |                                                                                                   
| :------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------------------------------: | 
|   <img src="https://avatars.githubusercontent.com/u/122000839?v=4" width=90px alt="김다은"/>       |   <img src="https://avatars.githubusercontent.com/cowboysj?v=4" width=90px alt="권채연"/>                       |
|   [@daeun084](https://github.com/daeun084)   |    [@cowboysj](https://github.com/cowboysj)  | 
| 숭실대학교 컴퓨터학부 | 경희대학교 컴퓨터공학과 | 

<br><br><br>


## Stack
**Language & Framework**  
<img src="https://img.shields.io/badge/Java-007396?style=flat&logo=Java&logoColor=white" />
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat&logo=SpringBoot&logoColor=white" /> 
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat&logo=SpringSecurity&logoColor=white" />


**Documentation**  
<img src="https://img.shields.io/badge/Rest Docs-6DB33F?style=flat&logo=Spring&logoColor=white" /> 
<img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat&logo=Swagger&logoColor=black" />

**Database & ORM**  
<img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=flat&logo=Spring&logoColor=white" /> 
<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white" /> 

**Build Tool**  
<img src="https://img.shields.io/badge/Gradle-02303A?style=flat&logo=Gradle&logoColor=white" />

**Cloud & Hosting**  
<img src="https://img.shields.io/badge/AmazonEC2-FF9900?style=flat&logo=AmazonEC2&logoColor=white" /> 
<img src="https://img.shields.io/badge/AmazonRDS-527FFF?style=flat&logo=AmazonRDS&logoColor=white" /> 

**Containerization & CI/CD**  
<img src="https://img.shields.io/badge/Docker-2496ED?style=flat&logo=Docker&logoColor=white" /> 
<img src="https://img.shields.io/badge/GithubActions-2088FF?style=flat&logo=GithubActions&logoColor=white" />

<br><br><br>


## Architecture
![image](https://github.com/user-attachments/assets/55b51461-42d4-4105-99bf-8f930a1266db) <br><br><br>


## ERD
![20](https://github.com/user-attachments/assets/b9384cc6-e3cf-4405-9c5f-057c09523ab5) <br><br><br>


## Convention

**commit convention** <br>
`conventionType: 구현한 내용` <br><br>


**convention Type** <br>
| convention type | description |
| --- | --- |
| `feat` | 새로운 기능 구현 |
| `add` | 파일 및 코드 추가 |
| `chore` | 부수적인 코드 수정 및 기타 변경사항 |
| `docs` | 문서 추가 및 수정, 삭제 |
| `fix` | 버그 수정 |
| `rename` | 파일 및 폴더 이름 변경 |
| `test` | 테스트 코드 추가 및 수정, 삭제 |
| `refactor` | 코드 리팩토링 |

<br><br><br>



## Branch
### 
- `컨벤션명/#이슈번호`
- pull request를 통해 develop branch에 merge 후, branch delete
- 부득이하게 develop branch에 직접 commit 해야 할 경우, `!hotfix:` 사용

<br><br><br>


## Directory

```PlainText
src/
├── main/
│   ├── common/
│   ├── enums/
│   ├── exception/
│   ├── response/
│   ├── status/
│   ├── annotation/
│   ├── utils/
│   ├── base/
│   ├── config/
├── entity/
├── controller/
├── service/
├── repository/
├── converter/
└── dto/
    ├── request/
    └── response/
		 
```

<br><br><br>




## 부하 테스트 
가장 요청이 많을 것으로 예상되는 `견적서 요청 API`에 대해 K6을 이용해 부하테스트를 진행했습니다.


|10명이 1초 동안 최대한의 요청을 보낸다.|2000명이 1초 동안 최대한의 요청을 보낸다.|
| :-------| :-------|
|<img width="888" alt="image" src="https://github.com/user-attachments/assets/699b7eeb-2863-4c94-8abe-832039674048">|<img width="821" alt="image" src="https://github.com/user-attachments/assets/468ae50b-542a-42cc-a6ee-0371404c8bd3">|
|- 총 119개의 요청이 문제없이 처리됨 <br>  - 평균 요청 처리 시간 : 83.75ms <br>  - 최소 요청 처리 시간 : 55.41ms <br>  - 최대 요청 처리 시간 : 160.37ms |- 총 2000개의 요청이 문제없이 처리됨<br> - 평균 요청 처리 시간 : 14.92s <br>  - 최소 요청 처리 시간 : 1.4s <br>  - 최대 요청 처리 시간 : 23.15s|

<br>

|사용자 수 변동 시나리오|응답 시간이 5초 이내인 최대 요청 수 파악|서버 복원력 측정|
| :-------|:----|:----|
|<img width="872" alt="image" src="https://github.com/user-attachments/assets/41330470-c418-48d3-ae4e-86a19eb17cef">|<img width="874" alt="image" src="https://github.com/user-attachments/assets/4344a709-b724-4d6e-9807-4b0bc6477b59">|<img width="844" alt="image" src="https://github.com/user-attachments/assets/723197ce-3ff8-41f5-84f7-d90b18377c1a">|
|0초 ~ 2초 : `50명`, 2초 ~ 12초 : `300명`, 12초 ~ 17초 : `1000명`, 17초 ~ 18초 : `500명`|5초가 지날 경우 사용자 이탈이 늘어날 것이라고 판단하여 1초 동안 500명의 사용자가 요청을 보내 `요청 처리 시간이 5초 이내`인 요청 개수를 파악 |서버에 과부하가 왔을 때 복원력을 측정
|- 총 2686개의 요청이 문제없이 처리됨 <br>  - 평균 요청 처리 시간 : 3.62s <br>  - 최소 요청 처리 시간 : 41.86ms <br>  - 최대 요청 처리 시간 : 9.19s |- 총 263개의 요청이 처리됨 <br> - 나머지 238개의 요청도 성공하였지만, 최대 요청 처리 시간으로 설정한 5초 이내에 응답이 오지 않음|- 1초 동안 3000명의 사용자 시나리오로 테스트를 진행했을 때, 일시적으로 서버에 과부하가 와 요청이 처리되지 않았음 <br> - 2초 정도 서버에 과부하가 왔지만, 이후 3초 이내에 바로 복원되는 것을 확인함 |


### 부하테스트 결과 분석

**1. 동시접속자 수가 많아질수록 요청 처리 시간이 늘어난다.**
- 동시접속자 수가 10명일 때의 평균 요청 처리 시간은 0.088초, 최대 0.16초이다.
- 동시접속자 수가 2000명일 때의 평균 요청 처리 시간은 14.92초, 최대 요청 처리 시간은 23.15초이다.

    현재 스마트인슈어런스 사이트에서 `견적 받기`를요청하는 사용자가 2달 동안 500여 명 정도인 것을 미루어 봤을 때, 요청 처리 시간이 오래 걸려 사용자 이탈이 일어날 확률은 아주 낮다.

**2. 스마트 인슈어런스의 사용자가 늘어날 것을 예상**
-  `사용자 수 변동 시나리오`를 통해 지속적으로 많은 사용자가 유입될 경우에도 요청이 문제 없이 잘 처리되는 것을 확인하였다. 
- 응답 시간이 5초가 넘어갈 경우, 사용자 이탈이 늘어날 것이라 예상해, `응답 시간이 5초 이내인 최대 요청 수 파악` 시나리오를 통해 확인한 결과, 1초에 263개의 요청을 모두 5초 이내에 처리할 수 있음을 파악하였다.
- `서버 복원력 측정` 시나리오를 통해, 동시접속자가 3000명일 때 서버에 일시적으로 과부하가 왔으나, 이내 3초만에 바로 복원되어 다음 요청이 잘 처리되는 것을 확인하였다. 






