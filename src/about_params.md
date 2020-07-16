# 注：所有参数都可以在系统根目录下的config.properties手动进行更改。

| 参数名               | 默认值                                               | 说明                                     |
| -------------------- | ---------------------------------------------------- | ---------------------------------------- |
| TeacherInfo          | D:\\workspace\\ArrangeCourse\\src\\TeacherInfo.xls   | 用于录入教师信息                         |
| ClassroomInfo        | D:\\workspace\\ArrangeCourse\\src\\ClassroomInfo.xls | 用于录入教室信息                         |
| ArrangeInfo          | D:\\workspace\\ArrangeCourse\\src\\ArrangeInfo.xls   | 用户录入学分安排                         |
| CourseInfo           | D:\\workspace\\ArrangeCourse\\src\\CourseInfo.xls    | 用户录入学院开设课程的指导文件           |
| savePath             | D:\\workspace\\ArrangeCourse\\src\\Answer.xls        | 用于存储生成的课表                       |
| savePath_course      | D:\\workspace\\ArrangeCourse\\src\\OutCourse.xls     | 用于显示所有被初始化过的课程信息         |
| classNum             | 8                                                    | 班级的个数（int）                        |
| studentsNum          | 60                                                   | 一个班级学生的个数（int）                |
| circumsNum           | 100                                                  | 循环迭代的次数                           |
| speciesNum           | 1                                                    | 同时生成课表的数目，种群数               |
| selectByPoint        | true                                                 | 开启学习效率优化                         |
| selectByDayAverage   | true                                                 | 开启每日平均学时优化                     |
| selectByHalfTerm     | true                                                 | 开启前后八周平均学时优化                 |
| crossoverPossibility | 0.8                                                  | 设置交叉的概率，该算法中没有用orz        |
| mutatePossibility    | 0.1                                                  | 设置变异的概率，用于使算法不落于局部优解 |

