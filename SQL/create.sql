-- 尚在开发阶段，故暂时不建立外键约束
create database FM;
use FM;
-- 1. Achievement（成就表）
CREATE TABLE Achievement (
    AchievementID INT PRIMARY KEY,
    Target INT,
    Name VARCHAR(255)
);

-- 2. Advise（教学关系表）
CREATE TABLE Advise (
    classID INT NOT NULL,
    coachID INT NOT NULL,
    traineeID INT NOT NULL,
    PRIMARY KEY (classID, coachID, traineeID)
);

-- 3. Book（预约表）
CREATE TABLE Book (
    bookID INT PRIMARY KEY AUTO_INCREMENT,
    classID INT NOT NULL,
    traineeID INT NOT NULL,
    paymentID INT,
    payMethod VARCHAR(50),
    bookStatus INT,
    bookTime DATETIME
);

-- 4. Coach（教练表）
CREATE TABLE Coach (
    coachID INT,
    userName VARCHAR(50),
    age INT,
    gender VARCHAR(10),
    iconURL VARCHAR(255),
    isMember INT,
    coachName VARCHAR(50),
    instructorHonors TEXT
);

-- 5. Comment（评论表）
CREATE TABLE Comment (
    commentID INT PRIMARY KEY AUTO_INCREMENT,
    userID INT NOT NULL,
    userName VARCHAR(50),
    postID INT NOT NULL,
    parentCommentID INT,
    commentTime DATETIME,
    likesCount INT,
    Content TEXT
);

-- 6. Course（课程表）
CREATE TABLE Course (
    classID INT PRIMARY KEY AUTO_INCREMENT,
    typeID INT,
    courseName VARCHAR(100),
    Capacity INT,
    courseDescription TEXT,
    coursePrice INT,
    courseStartTime DATETIME,
    courseEndTime DATETIME,
    courseLastDays INT,
    courseGrade DECIMAL(7,3),
    coursePhotoUrl VARCHAR(255),
    courseVideoUrl VARCHAR(255),
    features TEXT
);

-- 7. CourseSchedule（课程安排表）
CREATE TABLE CourseSchedule (
    classID INT NOT NULL,
    dayOfWeek INT,
    classTime VARCHAR(100),
    PRIMARY KEY (classID, dayOfWeek,classTime)
);

-- 8. CourseType（课程类型表）
CREATE TABLE CourseType (
    typeID INT PRIMARY KEY,
    typeName VARCHAR(100)
);

-- 9. Exercise（锻炼表）
CREATE TABLE Exercise (
    exerciseName VARCHAR(100) PRIMARY KEY,
    explanation TEXT,
    equipmentName VARCHAR(100),
    part VARCHAR(100),
    gifUrl VARCHAR(255),
    time INT,
    count INT,
    coverUrl VARCHAR(255)
);

-- 10. Fitness（健身数据表）
CREATE TABLE Fitness (
    userID INT PRIMARY KEY,
    height DECIMAL(5,3),
    weight DECIMAL(5,2),
    BMI DECIMAL(4,2),
    bodyFatRate DECIMAL(4,2)
);

-- 11. FitnessEquiOperation（健身器材表）
CREATE TABLE FitnessEquiOperation (
    equipmentName VARCHAR(100) PRIMARY KEY,
    operationGuide TEXT,
    briefIntr TEXT,
    createTime VARCHAR(255),
    lastUpdateTime VARCHAR(255),
    imgUrl VARCHAR(255),
    userRatings DECIMAL(6,3)
);

-- 12. FitnessSuggestion（健身建议表）
CREATE TABLE FitnessSuggestion (
    screenshotID INT PRIMARY KEY AUTO_INCREMENT,
    userID INT NOT NULL,
    createTime VARCHAR(255),
    exerciseName VARCHAR(100),
    screenshotUrl VARCHAR(255),
    suggestions TEXT
);

-- 13. FoodPlan（饮食计划表）
CREATE TABLE FoodPlan (
    foodPlanID INT PRIMARY KEY AUTO_INCREMENT,
    userID INT NOT NULL,
    createTime DATETIME,
    date DATE,
    mealType INT,
    state INT,
    numOfTypes INT,
    achievementGain INT
);

-- 14. FoodPlanFood（饮食计划食物表）
CREATE TABLE FoodPlanFood (
    foodPlanID INT NOT NULL,
    foodName VARCHAR(100) NOT NULL,
    foodAmount INT,
    PRIMARY KEY (foodPlanID, foodName)
);

-- 15. Foods（食物表）
CREATE TABLE Foods (
    foodName VARCHAR(100) PRIMARY KEY,
    calorie INT
);

-- 16. Friendship（好友关系表）
CREATE TABLE Friendship (
    userID INT NOT NULL,
    friendID INT NOT NULL,
    PRIMARY KEY (userID, friendID)
);

-- 17. MealRecords（饮食记录表）
CREATE TABLE MealRecords (
    recordID INT PRIMARY KEY AUTO_INCREMENT,
    userID INT NOT NULL,
    createTime DATETIME,
    mealTime DATETIME,
    mealPhoto VARCHAR(255),
    diningAdvice TEXT
);

-- 18. MealRecordsFood（饮食记录食物表）
CREATE TABLE MealRecordsFood (
    recordID INT NOT NULL,
    foodName VARCHAR(100) NOT NULL,
    foodAmount INT,
    PRIMARY KEY (recordID, foodName)
);

-- 19. Messages（消息表）
CREATE TABLE Messages (
    messageID INT PRIMARY KEY AUTO_INCREMENT,
    senderID INT NOT NULL,
    receiverID INT NOT NULL,
    messageType VARCHAR(20),
    Content TEXT,
    sendTime DATETIME,
    isRead INT
);

-- 20. Participate（参与表）
CREATE TABLE Participate (
    traineeID INT NOT NULL,
    classID INT NOT NULL,
    typeID INT,
    Grade INT,
    Evaluate TEXT,
    PRIMARY KEY (traineeID, classID)
);

-- 21. Payment（支付表）
CREATE TABLE Payment (
    paymentID INT PRIMARY KEY AUTO_INCREMENT,
    bookID INT NOT NULL,
    Amount INT,
    payMethod VARCHAR(100),
    paymentStatus INT,
    payTime DATETIME
);

-- 22. PhysicalTest（体能测试表）
CREATE TABLE PhysicalTest (
    userID INT PRIMARY KEY,
    pushups INT,
    squats INT,
    situps INT,
    pullups INT,
    longDistance INT
);

-- 23. Posts（帖子表）
CREATE TABLE Posts (
    postID INT PRIMARY KEY AUTO_INCREMENT,
    userID INT NOT NULL,
    postTitle VARCHAR(100),
    postContent TEXT,
    postCategory VARCHAR(100),
    postTime DATETIME,
    likesCount INT,
    forwardCount INT,
    commentsCount INT,
    refrencePostID INT,
    userName VARCHAR(100),
    imgUrl VARCHAR(255),
    isPinned INT,
    isReported INT
);

-- 24. Publish（发布表）
CREATE TABLE Publish (
    userID INT NOT NULL,
    postID INT PRIMARY KEY,
    publishTime DATETIME
);

-- 25. Recipes（食谱表）
CREATE TABLE Recipes (
    recipeID INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100),
    imgUrl VARCHAR(255),
    content TEXT,
    releaseTime DATETIME
);

-- 26. Teaches（教学关系表）
CREATE TABLE Teaches (
    coachID INT NOT NULL,
    classID INT NOT NULL,
    typeID INT,
    PRIMARY KEY (coachID, classID)
);

-- 27. Trainee（学员表）
CREATE TABLE Trainee (
    traineeID INT PRIMARY KEY,
    userName VARCHAR(50),
    Age INT,
    Gender VARCHAR(10),
    iconURL VARCHAR(255),
    traineeName VARCHAR(50)
);

-- 28. Update（更新课程内容表）
CREATE TABLE `Update` (
    coachID INT NOT NULL,
    classID INT NOT NULL,
    actionType VARCHAR(50),
    updateTime DATETIME,
    updateContext TEXT,
    PRIMARY KEY (coachID, classID)
);

-- 29. User（用户表）
CREATE TABLE User (
    userID INT PRIMARY KEY AUTO_INCREMENT,
    userName VARCHAR(100),
    password VARCHAR(255),
    email VARCHAR(100),
    registrationTime DATETIME,
    age INT,
    gender VARCHAR(10),
    isMember INT,
    isPost INT,
    isDelete INT,
    iconURL VARCHAR(255),
    tags VARCHAR(255),
    introduction TEXT,
    goalType VARCHAR(50),
    goalWeight DECIMAL(5,2),
    lastLoginTime DATETIME,
    role VARCHAR(20),
    vigorTokenBalance INT 
);

-- 30. UserAchievement（用户成就表）
CREATE TABLE UserAchievement (
    userID INT NOT NULL,
    achievementID INT NOT NULL,
    progress INT,
    achievedDate VARCHAR(255),
    isAchieved INT,
    PRIMARY KEY (userID, achievementID)
);

-- 31. UserFitnessPlan（用户健身计划表）
CREATE TABLE UserFitnessPlan (
    userID INT NOT NULL,
    workoutName VARCHAR(100),
    date DATE,
    isCompleted INT,
    PRIMARY KEY (userID, workoutName, date)
);

-- 32. UserFitnessPlanGoal（用户健身目标表）
CREATE TABLE UserFitnessPlanGoal (
    userID INT PRIMARY KEY,
    duration INT,
    planType VARCHAR(100)
);

-- 33. VigorTokenRecord（活力币记录表）
CREATE TABLE VigorTokenRecord (
    recordID INT PRIMARY KEY AUTO_INCREMENT,
    userID INT NOT NULL,
    reason VARCHAR(255),
    `change` INT,
    balance INT,
    createTime DATETIME
);

-- 34. Workout（锻炼计划表）
CREATE TABLE Workout (
    workoutName VARCHAR(100) PRIMARY KEY,
    exerciseName VARCHAR(100),
    coverUrl VARCHAR(255)
);

-- 35. Manager（管理员表）
CREATE TABLE Manager (
    email VARCHAR(100) PRIMARY KEY
);
