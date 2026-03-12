## CH 4 클라우드_아키텍처 설계 & 배포

### LV 0 - 요금 폭탄 방지 AWS Budget 설정
![img.png](img.png)

### LV 1 - 퍼플릭 IP
- 13.125.230.79
- ![img_1.png](img_1.png)
### LV 2 -DB 분리 및 보안 연결하기
- #### Actuator Info 엔드포인트 URL http://13.125.230.79:8080/actuator/info
![img_2.png](img_2.png)
- #### RDS 보안 그룹 캡쳐
![img_3.png](img_3.png)
### LV 3 - Presigned URL
#### - Presigned URL 만료 시간: 2026-03-19 19:19:53 KST
![img_6.png](img_6.png)
![img_7.png](img_7.png)
    
