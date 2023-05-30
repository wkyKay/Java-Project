import csv
import codecs
from datetime import *

# q1
num_q_haveanswers = 0
num_q_noanswers = 0
max_num_answers = 0
total_num_answers = 0
num_ans_dis = {}
# q2
total_ac_answers = 0
durations = []
ua = 0
dates = []
with codecs.open('data_active_new.csv', 'r', encoding='utf-8') as file:
    for row in csv.DictReader(file, skipinitialspace=True):
        if row['num_answer'] != '0':
            num_q_haveanswers += 1
            total_num_answers += int(row['num_answer'])
            if int(row['num_answer']) > max_num_answers:
                max_num_answers = int(row['num_answer'])
            if row['accepted'] == 'true':
                total_ac_answers += 1
                q_date = datetime.strptime(row['q_time'], "%Y-%m-%dT%H:%M:%S")
                ac_date = datetime.strptime(row['ac_ans_time'], "%Y-%m-%dT%H:%M:%S")
                duration = ac_date - q_date
                durations.append(duration)
        else:
            num_q_noanswers += 1

        if row['num_answer'] in num_ans_dis.keys():
            num_ans_dis[row['num_answer']] += 1
        else:
            num_ans_dis[row['num_answer']] = 1
        if row['ua'] == 'True':
            ua += 1
    with open('number_of_answers.csv', 'w', encoding='utf-8', newline='') as noa:
        writer_noa = csv.writer(noa)
        data = ["What percentage of questions don't have any answer?", str(num_q_noanswers / 5.0) + "%"]
        writer_noa.writerow(data)
        data = ["What is the average and maximum number of answers?", str(total_num_answers / 500), str(max_num_answers)]
        writer_noa.writerow(data)
    with open('answers_distribution.csv', 'w', encoding='utf-8', newline='') as ad:
        writer_ad = csv.writer(ad)
        writer_ad.writerow(['num_answer', 'frequency'])
        for key in num_ans_dis:
            data = [key, num_ans_dis[key]]
            writer_ad.writerow(data)
    with open('accepted_answers.csv', 'w', encoding='utf-8', newline='') as aa:
        writer_aa = csv.writer(aa)
        data = ["What percentage of questions have accepted answers (one question could only have one accepted answer)?", total_ac_answers / 5.0, "%"]
        writer_aa.writerow(data)
        data = ["What percentage of questions have non-accepted answers (i.e., answers that are not marked as accepted) that have received more upvotes than the accepted answers?", 1.0 * ua / total_ac_answers, "%"]
        writer_aa.writerow(data)
    with open('durations.csv', 'w', encoding='utf-8', newline='') as dd:
        writer_dd = csv.writer(dd)
        writer_dd.writerow(['days', 'seconds'])
        for d in durations:
            data = [d.days, d.seconds]
            writer_dd.writerow(data)
            dates.append(data)
    dates = sorted(dates, key=lambda x: (x[0], x[1]))
    # 统计各个时间段的dates数量
    time_counts = {}
    for date in dates:
        day = date[0]  # 提取日期
        if day in time_counts:
            time_counts[day] += 1
        else:
            time_counts[day] = 1

    # 写入新的CSV文件
    with open('duration_distribution.csv', 'w', encoding='utf-8', newline='') as ddd:
        writer_ddd = csv.writer(ddd)
        writer_ddd.writerow(['date', 'count'])
        for day, count in time_counts.items():
            writer_ddd.writerow([day, count])
    hour_counts = {}
    for date in dates:
        day = date[0]  # 提取日期
        seconds = date[1]  # 提取秒数
        hours = seconds // 3600  # 将秒数转换为小时数
        if day in hour_counts:
            hour_counts[day][hours] = hour_counts[day].get(hours, 0) + 1
        else:
            hour_counts[day] = {hours: 1}

    # 写入新的CSV文件
    with open('hour_counts.csv', 'w', encoding='utf-8', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(['date', 'hour', 'count'])
        for day, hour_data in hour_counts.items():
            for hour, count in hour_data.items():
                writer.writerow([day, hour, count])
    # print("What percentage of questions don't have any answer?", num_q_noanswers / 5.0, "%")
    # print("What is the average and maximum number of answers?", total_num_answers / 500)
    # print("What is the distribution of the number of answers?")  # 需要前端如何展示？
    # print()
    # print("What percentage of questions have accepted answers (one question could only have one accepted answer)?", total_ac_answers / 5.0, "%")
    # print("What is the distribution of question resolution time (i.e., the duration between the question posting time and the posting time of the accepted answer)?")
    # print(durations)
    # print("What percentage of questions have non-accepted answers (i.e., answers that are not marked as accepted) that have received more upvotes than the accepted answers?", 1.0 * ua / total_ac_answers, "%")
file.close()
