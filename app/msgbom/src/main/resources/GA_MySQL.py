#!/usr/bin/env python
# coding: utf-8

# In[1]:


import numpy as np  # 导入numpy数值计算扩展
import pandas as pd  # 导入操作数据集工具
import pymysql  # 导入数据库
# import matplotlib.pyplot as plt
import random
# import csv
import sys

random.seed(10)
from copy import copy


def getData(user, password,port, database, targetTable, commandText, value):  # 从数据库提取数据 commandText sql命令命令文本 format格式化字符串
    connection = pymysql.connect(host=db_url, user=user, passwd=password, port=port, db=database, charset='utf8')
    cursor = connection.cursor()  # cursor数据库交互对象
    try:
        cursor.execute(commandText.format(targetTable))  # 使用execute方法执行SQL语句 Execute 将字符串内容当做命令来执行
        if value:
            return cursor.fetchall()
        else:
            x = cursor.description  # 描述标签
            columns = [y[0] for y in x]
            cursor01 = cursor.fetchall()  # fetchall返回多个元组
            return pd.DataFrame(cursor01, columns=columns)  # Python读取mysql数据，转为DataFrame格式并根据原TABLE中的COLUMNS指定columns
    finally:
        cursor.close()
        connection.close()


# HYP DB
# user = 'root'
# password = 'Passw0rd@'
# db_url = '10.104.1.165'

# SZP DB
# user = 'a_infodba'
# password = 'Infodba@1234'
# db_url = '10.176.82.39'

# WHP DB
# user = 'esop'
# password = 'esop'
# db_url = '10.114.113.104'

# LSSC Test DB
# user = 'levaulttest'
# password = 'Sky123aton!20220609'
# port = 63706

user = 'nancal'
password = 'nancal.123'
port = 3306

db_url = 'mariadb'
database = 'lzdigit_rd_624'
targetTable = 'ga_task'

# 输入任务ID
# 方法一：读取数据库
# commandText ='select ID from GA_TASK'
# ID_list=getData(user,password,database,targetTable,commandText,False).ID.tolist()

# 方法二：程序内嵌
# ID_list = [143405539135619072]


# 方法三：.py执行时输入
# ID_list = list(map(int,input("Enter TaskID(以空格分隔): ").split()))
ID_list = map(int, sys.argv[1:])


# 排序遗传算法

# 初始化种群函数
def initPop():
    random.seed(10)
    init_pop = np.empty(shape=[0, chrom_size], dtype=int)
    circle_error = False

    for j in range(pop_size):
        in_degrees = dict((u, 0) for u in G)  # 初始化所有顶点入度为0
        vertex_num = len(in_degrees)
        for u in G:
            in_degrees[u] = len(G[u])  # 获取每个顶点的入度
        Q = [u for u in in_degrees if in_degrees[u] == 0]  # 筛选入度为0的顶点
        Seq = []
        while Q:
            u = Q.pop()  # 随机选择一个顶点
            Seq.append(u)
            temp = []
            for k in G:
                for v in G[k]:  # 移除其所有指向
                    if v == u:
                        in_degrees[k] -= 1
                if in_degrees[k] == 0 and (k not in Seq) and (k not in Q) and (k not in temp):  # 再次筛选入度为0的顶点
                    temp.append(k)
            random.shuffle(temp)
            for rand_node in temp:
                Q.append(rand_node)

        if len(Seq) == vertex_num:
            init_pop = np.append(init_pop, [Seq], axis=0)  # 输出一条初始化的排序染色体
        else:
            circle_error = True  # 工序的优先顺序存在矛盾，如果循环结束后存在非0入度的顶点说明图中有环，不存在拓扑排序
    return init_pop, circle_error


# 根据基因型矩阵求表现型矩阵
def Ge2Ph(Chrom):
    phen = np.zeros((pop_size, chrom_size))
    for i in range(len(Chrom)):
        k = 0
        for j in Chrom[i]:
            phen[i][k] = ge2ph[j]
            k = k + 1
    return phen


# 将表现型染色体，根据节拍要求，切分到各个工站
def groupProc(phen_ind, CT):
    pos = 0
    T_i = []  # 记录每一个工站的总时间
    Pos_i = []  # 记录切分位置

    for n in range(n_station):
        curr_sum = 0
        for j in range(pos, chrom_size):
            temp_sum = curr_sum + phen_ind[j]
            if n == n_station - 1:
                T_i.append(T - sum(T_i))
                Pos_i.append(chrom_size - 1)
                break
            else:
                if temp_sum > CT:
                    if CT <= max_T and j == pos:
                        pos = j + 1
                        T_i.append(temp_sum)
                        Pos_i.append(pos)
                        break
                    else:
                        pos = j
                        T_i.append(curr_sum)
                        Pos_i.append(pos)
                        break
                else:
                    curr_sum = temp_sum
    return T_i, Pos_i


# 计算单个表现型基因的适应度即（LineBalance）
def calFitness(n_station, T, phen_ind, gene_ind, if_visual):
    CT = T / n_station
    opt_CT = int(CT / 0.95)  # 初始化理论最优节拍

    for iter_num in range(10000):
        # 1 按顺序分组
        T_i, Pos_i = groupProc(phen_ind, CT)
        # 2 判断当前分组是否满足节拍要求
        if max(T_i) <= CT:
            limit = CT
            while True:
                if len(Pos_i) == n_station:
                    opt_CT = CT
                    Best_Pos = Pos_i
                    Best_T = T_i
                    break
                else:
                    limit = limit * 0.98
                    T_i, Pos_i = groupProc(phen_ind, limit)
            break
        else:
            CT = max(T_i)
        # 3 计算新的节拍CT_temp
        T_i_temp = []

        for n in range(len(Pos_i)):
            if n < (len(Pos_i) - 1):
                T_i_temp.append(T_i[n] + phen_ind[Pos_i[n]])
        CT_temp = min(T_i_temp)

        # 4 选择新的节拍
        if CT > CT_temp:
            CT = CT_temp
        else:
            limit = CT  # limit变量的作用：当节拍=瓶颈工序时间时，需要让工序尽可能平均分到其余工位
            while True:
                if len(Pos_i) == n_station:
                    opt_CT = CT
                    Best_Pos = Pos_i  # 最优工站切割点
                    Best_T = T_i  # 最优情况下各工位的工时
                    break
                else:
                    limit = limit * 0.98  # 瓶颈工序的工位固定后，逐步放开CT限制
                    T_i, Pos_i = groupProc(phen_ind, limit)
            break

    line_balance = T / (opt_CT * n_station)  # 不考虑固定工位的线平衡
    # 按最优节拍，分配工序到各个工站
    if constrains or if_visual:
        Best_Pos.insert(0, 0)
        part = []  # 记录单个工站中的工序（index形式）
        station_op = []  # 汇总所有工站中工序
        for i in range(n_station):
            n1 = Best_Pos[i]
            n2 = Best_Pos[i + 1]
            if i == n_station - 1:
                part.append(gene_ind[n1:])
            else:
                part.append(gene_ind[n1:n2])
            if if_visual:
                out_list = []  # 记录各个工站中的工序（opid形式）
                for j in part[i]:
                    out_list.append(op_dict[j - 1])
                    Best_T[i] = round(Best_T[i], 2)
                station_op.append(out_list)

        # 判断是否符合工位工序的约束关系
        for constrain in constrains:
            procedure = constrain[0]
            station = constrain[1]
            if procedure not in part[station - 1]:
                line_balance = line_balance * 0.1  # 如果与约束矛盾，惩罚机制，降低线平衡

        # 适应度输出
        if if_visual:
            return line_balance, opt_CT, Best_T, station_op, np.var(Best_T)

        # 工站时间的方差
    return line_balance


# 计算当前种群适应度矩阵
def calFitnv(chrom_array):
    phen = Ge2Ph(chrom_array)  # 基因型转表现型
    fitness = []  # 记录每个个体的适应度

    for i in range(pop_size):  # 个体适应度计算
        gene_ind = chrom_array[i]
        phen_ind = phen[i]
        fitness.append(calFitness(n_station, T, phen_ind, gene_ind, False))

    Fitnv = np.array(fitness)  # 输出格式转换
    Fitnv.resize(pop_size, 1)
    return Fitnv


# 基因重组：两个父代产生两个子代（改进的顺序重组）
def geRecombi(list1, list2, index):
    random.seed(index)
    length = len(list1)
    status = True
    while status:  # 生成切片随机数
        k1 = random.randint(0, length)
        k2 = random.randint(0, length)
        if k1 < (k2 - 1):
            status = False
        if k2 < (k1 - 1):
            t = k2
            k2 = k1
            k1 = t
            status = False
    # 截取片段
    fragment1 = list1[k1: k2]
    fragment2 = list2[k1: k2]
    # 未截取片段
    remain1 = list(set(list1).difference(fragment1))
    remain2 = list(set(list2).difference(fragment2))
    temp_list1 = copy(list1)
    temp_list2 = copy(list2)
    # 交叉
    for value in remain1:
        temp_list2.remove(value)
    for value in remain2:
        temp_list1.remove(value)
    # 生成子代
    list1[k1: k2] = temp_list2
    list2[k1: k2] = temp_list1
    return list1, list2


# 基因变异：从染色体随机位置开始，之后的基因重新随机生成
def geMuta(graph, list_mu, index):
    random.seed(index)
    # 前段（保留）
    k = random.randint(0, len(list_mu) - 1)
    Seq = list_mu[:k]
    # 初始化所有顶点入度为0，逻辑与初始化染色体相同
    in_degrees = dict((u, 0) for u in graph)
    for u in graph:
        in_degrees[u] = len(graph[u])
    # 根据保留片段，重置入度
    for u in Seq:
        for k in graph:
            for v in graph[k]:
                if v == u:
                    in_degrees[k] -= 1

    Q = [u for u in in_degrees if (in_degrees[u] == 0 and (u not in Seq))]  # 筛选入度为0的顶点
    while Q:
        u = random.choice(Q)
        Q.remove(u)
        Seq.append(u)
        for k in graph:
            for v in graph[k]:
                if v == u:
                    in_degrees[k] -= 1
            if in_degrees[k] == 0 and (k not in Seq) and (k not in Q):
                Q.append(k)  # 再次筛选入度为0的顶点
    return Seq


# 种群（二维矩阵）基因重组，两两重组
def Recombi(parents_array, recombi_rate, index):
    random.seed(index)
    parents = pd.DataFrame(parents_array)
    offsprings = np.empty(shape=[0, chrom_size], dtype=int)
    s = [x for x in range(0, int(pop_size * gap))]
    for i in range(int(pop_size * gap / 2)):
        # 随机选择两个DNA
        n1 = random.sample(s, 1)[0]
        s.remove(n1)
        n2 = random.sample(s, 1)[0]
        s.remove(n2)
        chrom1 = parents.loc[n1, :].values.tolist()
        chrom2 = parents.loc[n2, :].values.tolist()
        parents.drop([n1, n2], inplace=True)
        # 根据重组概率决定是否发生重组
        if random.random() < recombi_rate:
            offspring1, offspring2 = geRecombi(chrom1, chrom2, random.random())
        else:
            offspring1, offspring2 = chrom1, chrom2
        # 加入子代种群
        offsprings = np.append(offsprings, [offspring1], axis=0)
        offsprings = np.append(offsprings, [offspring2], axis=0)
    return offsprings


# 种群（二维矩阵）基因突变
def Mutation(parents, muta_rate, index):
    random.seed(index)
    offsprings_mu = np.empty(shape=[0, chrom_size], dtype=int)
    s = [x for x in range(0, int(pop_size * gap))]
    for i in range(int(pop_size * gap)):
        chrom = parents[i].tolist()
        if random.random() < muta_rate:
            offspring = geMuta(G, chrom, random.random())
        else:
            offspring = chrom
        offsprings_mu = np.append(offsprings_mu, [offspring], axis=0)
    return offsprings_mu


def initPop_fix_stage3(nsta):
    random.seed(10)
    init_pop = np.empty(shape=[0, chrom_size], dtype=int)
    pos_list = [i for i in range(1, chrom_size + 1)]  # 所有切割位置编号
    for j in range(pop_size):
        ind = [0 for i in range(1, chrom_size + 1)]  # 生成全0序列
        cut_pos = random.sample(pos_list, nsta - 1)  # 将切分位置设为1
        for i in cut_pos:
            ind[i - 1] = 1
        init_pop = np.append(init_pop, [ind], axis=0)
    return init_pop


# 分配遗传算法

# 初始化种群，切分算法中染色体表现型=基因型
def initPop_fix():
    random.seed(10)
    init_pop = np.empty(shape=[0, chrom_size], dtype=int)
    pos_list = [i for i in range(1, chrom_size + 1)]  # 所有切割位置编号
    for j in range(pop_size):
        ind = [0 for i in range(1, chrom_size + 1)]  # 生成全0序列
        cut_pos = random.sample(pos_list, n_station - 1)  # 将切分位置设为1
        for i in cut_pos:
            ind[i - 1] = 1
        init_pop = np.append(init_pop, [ind], axis=0)
    return init_pop


def initPop_fix_pro(curr_opt):
    random.seed(10)
    init_pop = np.empty(shape=[0, chrom_size], dtype=int)
    pos_list = [i for i in range(1, chrom_size + 1)]  # 所有切割位置编号
    for j in range(pop_size):
        ind = [0 for i in range(1, chrom_size + 1)]  # 生成全0序列
        if j >= 1:
            cut_pos = random.sample(pos_list, n_station - 1)  # 将切分位置设为1
        else:
            cut_pos = curr_opt
        for i in cut_pos:
            ind[i - 1] = 1
        init_pop = np.append(init_pop, [ind], axis=0)
    return init_pop


# 双基因重组：两个父代产生两个子代（两点截取等长片段交叉）
def geRecombi_fix(list1, list2, index):
    random.seed(index)
    length = len(list1)
    while True:
        status = True
        # 选取变异段
        while status:
            k1 = random.randint(0, length)
            k2 = random.randint(0, length)
            if k1 < k2:
                status = False
            if k2 < k1:
                t = k2
                k2 = k1
                k1 = t
                status = False
        # 截取片段
        fragment1 = list1[k1: k2]
        fragment2 = list2[k1: k2]
        if fragment1.count(1) == fragment2.count(1):  # 保证截取片段中的切分点个数一致
            # 交叉
            list1[k1: k2] = fragment2
            list2[k1: k2] = fragment1
            break
    return list1, list2


# 单基因变异：随机选取一个片段，在该片段内，随机洗牌
def geMuta_fix(list_mu, index):
    random.seed(index)
    length = len(list_mu)
    status = True
    # 选取变异段
    while status:
        k1 = random.randint(0, length)
        k2 = random.randint(0, length)
        if k1 < k2:
            status = False
        if k2 < k1:
            t = k2
            k2 = k1
            k1 = t
            status = False
    # 截取片段后随机洗牌
    fragment = list_mu[k1: k2]
    random.shuffle(fragment)
    list_mu[k1: k2] = fragment
    return list_mu


# 种群（二维矩阵）基因重组
def Recombi_fix(parents_array, recombi_rate, index):
    random.seed(index)
    parents = pd.DataFrame(parents_array)
    offsprings = np.empty(shape=[0, chrom_size], dtype=int)
    s = [x for x in range(0, int(pop_size * gap))]
    for i in range(int(pop_size * gap / 2)):
        n1 = random.sample(s, 1)[0]
        s.remove(n1)
        n2 = random.sample(s, 1)[0]
        s.remove(n2)
        chrom1 = parents.loc[n1, :].values.tolist()
        chrom2 = parents.loc[n2, :].values.tolist()
        parents.drop([n1, n2], inplace=True)
        if random.random() < recombi_rate:
            offspring1, offspring2 = geRecombi_fix(chrom1, chrom2, random.random())
        else:
            offspring1, offspring2 = chrom1, chrom2
        offsprings = np.append(offsprings, [offspring1], axis=0)
        offsprings = np.append(offsprings, [offspring2], axis=0)
    return offsprings


# 种群（二维矩阵）基因突变
def Mutation_fix(parents, muta_rate, index):
    random.seed(index)
    offsprings_mu = np.empty(shape=[0, chrom_size], dtype=int)
    s = [x for x in range(0, int(pop_size * gap))]
    for i in range(int(pop_size * gap)):
        chrom = parents[i].tolist()
        if random.random() < muta_rate:
            offspring = geMuta_fix(chrom, random.random())
        else:
            offspring = chrom
        offsprings_mu = np.append(offsprings_mu, [offspring], axis=0)
    return offsprings_mu


# 求单个表现型基因的适应度
def calFitness_fix(gene_ind, if_visual, nsta):
    n1 = 0  # 切分起始位置
    n2 = 1  # 切分终止位置
    part = []
    time_part = []
    station_op = []
    for i in gene_ind:
        if i == 1:
            part.append(proc_list[n1:n2])
            time_part.append(proc_time[n1:n2])
            n1 = n2
        n2 += 1
    part.append(proc_list[n1:])
    time_part.append(proc_time[n1:])
    # 计算每个工站的时间
    tack_time = []
    for i in time_part:
        tack_time.append(round(sum(i), 2))

    max_t = max(tack_time)  # 计算节拍时间
    line_balance = T / (max_t * nsta)
    line_var = np.var(tack_time)

    for constrain in constrains:
        procedure = constrain[0]
        station = constrain[1]
        if procedure not in part[station - 1]:
            line_var = line_var * 2
            line_balance = line_balance * 0.1

    if if_visual:
        for i in range(nsta):
            outlist = []
            for j in part[i]:  # 转换为opid
                outlist.append(op_dict[j - 1])
            station_op.append(outlist)
        return line_balance, max_t, tack_time, station_op, line_var  # 产线节拍max_t,工站时间tack_time,工站工序station_op

    return line_var


# 求单个表现型基因的适应度
def calFitness_fix_stage3(gene_ind, if_visual, nsta):
    n1 = 0  # 切分起始位置
    n2 = 1  # 切分终止位置
    part = []
    time_part = []
    station_op = []
    for i in gene_ind:
        if i == 1:
            part.append(proc_list[n1:n2])
            time_part.append(proc_time[n1:n2])
            n1 = n2
        n2 += 1
    part.append(proc_list[n1:])
    time_part.append(proc_time[n1:])
    # 计算每个工站的时间
    tack_time = []
    for i in time_part:
        tack_time.append(round(sum(i), 2))

    max_t = max(tack_time)  # 计算节拍时间
    line_balance = T / (max_t * nsta)
    line_var = np.var(tack_time)

    if if_visual:
        for i in range(nsta):
            outlist = []
            for j in part[i]:  # 逆向转化
                if isinstance(package_op_dict[j], list):
                    outlist += package_op_dict[j]
                else:
                    outlist.append(package_op_dict[j])
            out_real_list = []
            for k in outlist:
                out_real_list.append(op_dict[k - 1])
            station_op.append(out_real_list)
        return line_balance, max_t, tack_time, station_op, line_var  # 产线节拍max_t,工站时间tack_time,工站工序station_op

    return line_var


# 当前种群适应度矩阵
def calFitnv_fix(chrom_array, nsta):
    fitness = []

    for i in range(pop_size):
        gene_ind = chrom_array[i]
        fitness_fix = calFitness_fix(gene_ind, False, nsta)
        if fitness_fix != 0:
            fitness.append(1 / fitness_fix)

    Fitnv = np.array(fitness)
    Fitnv.resize(pop_size, 1)

    return Fitnv


def calFitnv_fix_stage3(chrom_array, nsta):
    fitness = []

    for i in range(pop_size):
        gene_ind = chrom_array[i]
        fitness.append(1 / (calFitness_fix_stage3(gene_ind, False, nsta) + 0.001))

    Fitnv = np.array(fitness)
    Fitnv.resize(pop_size, 1)

    return Fitnv


# 选择算子
# sus随机选择
def sus_selecting(chrom_array, sel_num, index):
    sel_num = int(sel_num)
    random.seed(index)
    selected_array = []
    for i in random.sample(list(range(sel_num)), sel_num):
        selected_array.append(chrom_array[i])
    return selected_array


# dup最优排序选择
def dup_selecting(chrom_array, parFitnV, sel_num):
    zipped = zip(parChrom.tolist(), parFitnV.tolist())
    # sorted(iterable,key,reverse)
    sort_zipped = sorted(zipped, key=lambda x: (x[1]), reverse=True)
    result = zip(*sort_zipped)
    x_axis, y_axis = [list(x) for x in result]
    temp = np.array(x_axis)
    selected_array = temp[0:(int(sel_num) + 1)]
    return selected_array


# 限定节拍中：计算可行工位数范围
def cal_n_station(phen_ind, CT):
    pos = 0
    T_i = []
    for n in range(max_n_station):
        curr_sum = 0
        if (T - sum(T_i)) < CT:
            T_i.append(T - sum(T_i))
            break
        for j in range(pos, chrom_size):
            temp_sum = curr_sum + phen_ind[j]
            if temp_sum > CT:
                pos = j
                T_i.append(curr_sum)
                break
            else:
                curr_sum = temp_sum
    return len(T_i)


# 瓶颈工位微调
def adjust_limit_station(station_time, max_T, station_op):
    # 前后挪动瓶颈工序
    flag_4 = True
    while flag_4:
        limit_station = station_time.index(max_T)  # 找到瓶颈工位
        limit_station_op = station_op[limit_station]
        temp_list = list(limit_station_op)
        flag_1 = 0
        flag_2 = 0
        for i in temp_list:
            flag_1 += 1
            if limit_station == 0:
                break
            if op_constrain_dict[i] != op_constrain_dict[i]:
                # 向前移动改工序
                if station_time[limit_station - 1] + op_time_dict[i] >= station_time[limit_station] - op_time_dict[i]:
                    break
                else:
                    station_time[limit_station - 1] += op_time_dict[i]
                    station_time[limit_station] -= op_time_dict[i]
                    station_op[limit_station - 1].append(i)
                    del station_op[limit_station][0]
            else:
                break
        for i in reversed(temp_list):
            flag_2 += 1
            if limit_station == len(station_time) - 1:
                break
            if op_constrain_dict[i] != op_constrain_dict[i]:
                # 向后移动改工序
                if station_time[limit_station + 1] + op_time_dict[i] >= station_time[limit_station] - op_time_dict[i]:
                    break
                else:
                    station_time[limit_station + 1] += op_time_dict[i]
                    station_time[limit_station] -= op_time_dict[i]
                    station_op[limit_station + 1].insert(0, i)
                    del station_op[limit_station][-1]
            else:
                break
        max_T = max(station_time)
        if flag_1 == 1 and flag_2 == 1:
            flag_4 = False
    return station_time, max_T, station_op


# In[2]:


# print("Start: ", time.strftime("%Y-%m-%d %H:%M:%S", time.localtime()))
for ID in ID_list:
    # print('--------------------------------------------------')
    # print("ID:", ID)

    # 从数据库中读取数据
    commandText = "SELECT FIXSTATION FROM ga_task WHERE ID='" + str(ID) + "'"
    isFixStation = getData(user, password,port, database, targetTable, commandText, True)[0][0]
    commandText = "SELECT EMPTYSTATION FROM ga_task WHERE ID='" + str(ID) + "'"
    empty_stations = list(map(int,getData(user, password,port, database, targetTable, commandText, True)[0][0].split(",")))
    if isFixStation == 0:
        commandText = "SELECT OPID,r.LINEID,r.OPNUM,r.PREOP,r.OPTIME,r.FIXSTATION,l.STATIONNUM FROM (SELECT o.ID as OPID,o.TASKID,o.OPNUM,o.PREOP,o.OPTIME,o.FIXSTATION,o.LINEID FROM ga_operation o WHERE o.PARENTID IS NULL) r LEFT JOIN ga_task t ON t.ID=r.TASKID LEFT JOIN ga_assemline l ON l.ID=r.LINEID WHERE t.ID='" + str(
            ID) + "' ORDER BY t.ID,l.ID,r.OPID"
        data = getData(user, password,port, database, targetTable, commandText, False)
    else:
        commandText = "SELECT OPID,r.LINEID,r.OPNUM,r.PREOP,r.OPTIME,r.FIXSTATION,l.MINSTATIONNUM,l.MAXSTATIONNUM,t.EXPECTEDTACTTIME FROM (SELECT o.ID AS OPID,o.TASKID,o.OPNUM,o.PREOP,o.OPTIME,o.FIXSTATION,o.LINEID FROM ga_operation o WHERE o.PARENTID IS NULL) r LEFT JOIN ga_task t ON t.ID=r.TASKID LEFT JOIN ga_assemline l ON l.ID=r.LINEID WHERE t.ID='" + str(
            ID) + "' ORDER BY t.ID,l.ID,r.OPID"
        data = getData(user, password,port, database, targetTable, commandText, False)


    # 无数据报错
    if data.shape[0] == 0:
        task_status = 2
        task_log = '无输入数据'
        # print('task_log', task_log)
        continue

    # 生成对应关系字典
    op_time_dict = dict(zip(data['OPID'].tolist(), data['OPTIME'].tolist()))  # 生成原始工序编码与工序时间对应字典
    op_dict = dict(zip(data.index.tolist(), data['OPID'].tolist()))  # 生成原始工序编码与工序序号ID对应字典
    op_constrain_dict = dict(zip(data['OPID'].tolist(), data['FIXSTATION'].tolist()))  # 生成原始工序编码与固定工位对应字典

    data.drop(['OPID'], axis=1, inplace=True)

    # 生成任务Job结果记录器:一个task中的一条线为一个Job
    Line_list = data['LINEID'].drop_duplicates().tolist()  # 获取Job
    task_status = 1  # 起始任务状态
    task_log = ''
    job_linebalance = []
    job_tacttime = []
    job_stationnum = []
    job_station_time = []
    job_station_op = []
    job_status = []

    # 开始执行Job
    for j in Line_list:
        # time_start=time.time()
        df = data[:][data.LINEID == j]
        df.drop(['LINEID'], axis=1, inplace=True)

        # 固定工位数情况
        if isFixStation == 0:
            # 设定GA参数，普通情况（不存在工位约束）
            gap = 0.8  # 代沟，gap%的父代被选作母体用于交叉变异
            pop_size = 20  # 种群大小
            re_rate = 0.7  # 交叉概率
            mu_rate = 0.3  # 变异概率
            iter_num = 200  # 迭代次数
            direct_stage_3 = 0

            # 读取工位数
            n_station = df['STATIONNUM'].values[0]
            n_station_ori = df['STATIONNUM'].values[0]
            if df['FIXSTATION'].max() > n_station:
                job_status.append(2)
                task_log = task_log + ' ' + '固定工位错误：存在固定的工位序号，超出当前设定工位数'
                continue
            if n_station >= len(df):  # 工序少于设定工位数，则工位数=工序数
                n_station = len(df)
            if df['FIXSTATION'].max() > n_station:  # 存在固定的工位序号，超出当前设定工位数
                n_station_ori = n_station
                n_station = int(df['FIXSTATION'].max())
                direct_stage_3 = 1

            # 读取工时
            T = df['OPTIME'].sum()  # 总工时
            max_T = df['OPTIME'].max()  # 瓶颈工序
            theoretical_best_line_balance = T / (max_T * n_station)  # 理论最优线平衡
            # if n_station >= len(df) and df['FIXSTATION'].max() <= n_station_ori:  # 工序少于设定工位数，则工位数=工序数
            #     n_station = len(df)
            #     job_stationnum.append(n_station)
            #     opt_linebalance = T / (n_station * max_T)
            #     job_linebalance.append(opt_linebalance)
            #     job_tacttime.append(max_T)
            #     temp = []
            #     for y in df['OPTIME'].tolist():
            #         temp.append(round(y, 2))
            #     job_station_time.append(temp)
            #     temp = []
            #     for j in df.index.tolist():  # 转换为opid
            #         temp.append([op_dict[j]])
            #     job_station_op.append(temp)
            #     job_status.append(3)
            #     continue

            # 如果只有一个工位，直接输出
            if n_station == 1:
                task_log = task_log + ' ' + 'Task' + str(ID) + ', 线体' + str(j) + ':只有一个工位'
                job_stationnum.append(n_station)
                opt_linebalance = 1
                job_linebalance.append(1)  # 线平衡为1
                job_tacttime.append(T)  # 节拍为工序总时间
                job_station_time.append([T])
                temp = []
                for j in df.index.tolist():  # 转换为opid
                    temp.append(op_dict[j])
                job_station_op.append([temp])
                job_status.append(3)
                continue

            # 炸开前序工序列
            try:
                df = pd.concat([df, df['PREOP'].str.split(',', expand=True)], axis=1)  # 炸开PREOP列
            except AttributeError:  # 如果前序信息列为空，则默认为串行
                for n in range(1, len(df) - 1):
                    df.loc[n, 'PREOP'] = df.loc[n - 1, 'OPNUM']
                df = pd.concat([df, df['PREOP'].str.split(',', expand=True)], axis=1)
                # task_log = task_log + ' ' + 'Task' + str(ID) + ', 线体' + str(j) + ':无前序信息，默认串行'
            df.drop(['PREOP'], axis=1, inplace=True)

            # 用自然数重新给工序编号
            for index, row in df.iterrows():
                df = df.replace(row['OPNUM'], index + 1)

            # 删除A线与B线之间跨线的工序关系，每条线单独优化（待开发：双线协同优化）
            df.iloc[:, 4:] = df.iloc[:, 4:].applymap(lambda x: x if x in df['OPNUM'].tolist() else None)

            # 读取工序工位约束
            constrains = df[['OPNUM', 'FIXSTATION']][df['FIXSTATION'].notnull()].astype(int).values.tolist()
            constrained_op = []
            constrained_stations = df['FIXSTATION'][df['FIXSTATION'].notnull()].astype(int).values.tolist()
            
            #存在空工位时，数据编号重新处理
            if len(empty_stations) >=1 and empty_stations[0]!=9999:
                #判断是否存在空工位和固定工位冲突
                for empty_station in empty_stations:
                    if empty_station in constrained_stations:
                        task_log = task_log + ' ' + 'Task' + str(ID) + ', 线体' + str(j) + ':人工选定的空工位上有固定工序'
                        job_status.append(2)
                        break
                n_station=n_station-len(empty_stations)
                for empty_station in empty_stations:
                    for idx, constrained_station in enumerate(constrained_stations):
                        if constrained_station > empty_station:
                            constrains[idx][1]-=1
                         
            # 备份初始固定工位
            constrains_for_check = constrains.copy()
            for opid in df['OPNUM'][df['FIXSTATION'].notnull()].astype(int).values.tolist():
                constrained_op.append(op_dict[opid - 1])
            # print(constrained_op)

            # 判断串行、是否有固定工位，进入不同算法
            series = False
            if df.shape[1] == 5:
                # if df.shape[1] == 5 and constrains:  # 串行且存在固定工位
                series = True

            if constrains and not series:  # 存在工序固定,但非串行,增加迭代和变异概率
                pop_size = 20
                re_rate = 0.6  # 交叉概率
                mu_rate = 0.4  # 变异概率
                iter_num = 200  # 迭代次数
            df.drop(['FIXSTATION'], axis=1, inplace=True)
            df.drop(['STATIONNUM'], axis=1, inplace=True)

            # 防止如下情况：工位数设定为25，最后一个工序缺固定在23号工位
            if constrains:
                if constrains[-1][0] == len(df) and n_station > constrains[-1][1]:
                    task_log = task_log + ' ' + 'Task' + str(ID) + ', 线体' + str(j) + ':设定工位数>最后一个工序固定的工位号'
                    n_station = constrains[-1][1]
            job_stationnum.append(n_station)

            # 如果串行直接输出结果
            if series:
                pop_size = 2  # 种群大小
                re_rate = 0  # 交叉概率
                mu_rate = 0  # 变异概率
                gap = 1
                iter_num = 1  # 迭代次数
            # 初始化AOV图（种群初始化和变异中要用）
            temp = df.fillna(0).drop(['OPTIME'], axis=1).astype(int).set_index('OPNUM').T.to_dict('list')
            G = {}

            for key, value in temp.items():
                G[key] = [i for i in value if i != 0]
            # 生成基因型表现型转换矩阵
            ge2ph = dict(zip(df['OPNUM'].tolist(), df['OPTIME'].tolist()))
            chrom_size = len(df)  # 染色体长度

            # 初始化种群
            try:
                population, circle_error = initPop()  # 输出初始化种群基因型矩阵
                stage_3_inputs = population  # 保留一个可行顺序

            except ValueError:
                task_log = task_log + ' ' + 'Task' + str(ID) + ', 线体' + str(j) + ':原始数据有误'
                job_status.append(2)
                continue
            if circle_error == True:
                task_log = task_log + ' ' + 'Task' + str(ID) + ', 线体' + str(j) + ':内有矛盾工序优先关系'
                job_status.append(2)
                continue

            # 初始化记录器
            df.set_index(["OPNUM"], inplace=True)
            gen = 0  # 进化代数
            opt_line_balance = 0  # 当前代数最优线平衡率
            Lind = int(chrom_size)  # 计算染色体长度
            obj_trace = np.zeros((iter_num, 2))  # 定义目标函数值记录器
            var_trace = np.zeros((iter_num, Lind))  # 染色体记录器，记录历代最优个体的染色体
            second_phase_pass = False

            if direct_stage_3 == 1:
                stage_3 = 1
            else:
                # 开始进化
                while True:
                    # 更新父代
                    parChrom = population
                    # 计算适应度矩阵
                    parFitnV = calFitnv(parChrom)
                    # 记录当前种群信息
                    best_ind = np.argmax(parFitnV)  # 计算当代最优个体的序号
                    obj_trace[gen, 0] = np.sum(parFitnV) / parFitnV.shape[0]  # 记录当代种群的目标函数均值
                    obj_trace[gen, 1] = parFitnV[best_ind]  # 记录当代种群最优个体目标函数值
                    opt_line_balance = obj_trace[gen, 1]
                    var_trace[gen, :] = parChrom[best_ind, :]  # 记录当代种群最优个体的染色体
                    if theoretical_best_line_balance == parFitnV[best_ind]:
                        # task_log = task_log + ' ' + 'Task' + str(ID) + ', 线体' + str(j) + ':产线节拍等于瓶颈工序时间,可缩小工站数'
                        iter_num = 1  # 如果工序太少，则遗传算法无法进行优化，因为产线节拍已经等于瓶颈工序时长
                        second_phase_pass = True

                    # 从父代选择进入交叉变异的个体
                    SelCh = sus_selecting(parChrom, pop_size * gap, random.random())

                    # 交叉与变异
                    offChrom = Mutation(Recombi(SelCh, re_rate, random.random()), mu_rate, random.random())
                    # 选出父代中的精英个体
                    best_parChrom = dup_selecting(parChrom, parFitnV, pop_size * (1 - gap))
                    # 父代精英个体与子代合并，形成新的种群
                    population = np.append(best_parChrom, offChrom, axis=0)

                    # 停止机制
                    stop_gen = 30  # 当连续30代不发生进化
                    if constrains:
                        stop_gen = 100

                    if gen > stop_gen:
                        if obj_trace[gen, 1] == obj_trace[gen - stop_gen, 1]:
                            break

                    gen = gen + 1  # 进化代数到达设定值

                    if gen == iter_num:
                        break

                    if opt_line_balance > 0.9:
                        break

                # 结果输出
                best_gen = np.argmax(obj_trace[:, [1]])
                opt_gene = list(map(int, var_trace[[best_gen], :][0].tolist()))  # 最优排程

                temp1 = Ge2Ph(var_trace[[best_gen], :])
                opt_phen = temp1[0]
                opt_linebalance1, res12, res13, res14, res15 = calFitness(n_station, T, opt_phen, opt_gene,
                                                                          True)  # 解码得到表现型

                # print("Phase I Finish")

                trans_temp = []
                curr_opt = []
                tempsum = 0
                # 解析第一阶段结果
                for i in res14:
                    trans_temp.append(len(i))
                for i in trans_temp:
                    tempsum += i
                    curr_opt.append(tempsum)
                del (curr_opt[-1])

                # if second_phase_pass == False:
                # 开始切分，平均分配到各个工位
                chrom_size = len(opt_gene) - 1  # 染色体长度
                proc_time = opt_phen  # 每个工序时间
                proc_list = opt_gene  # 工序列表

                gap = 0.8
                pop_size = 60  # 种群大小
                re_rate = 0.6  # 交叉概率
                mu_rate = 0.4  # 变异概率
                population = initPop_fix_pro(curr_opt)

                if opt_linebalance1 > 0.1:
                    iter_num = 300
                else:
                    iter_num = 700  # 迭代次数

                if second_phase_pass == True:
                    iter_num = 30

                gen = 0  # 进化代数
                opt_line_balance = 0  # 当前代数最优线平衡率
                Lind = int(chrom_size)  # 计算染色体长度
                obj_trace = np.zeros((iter_num, 2))  # 定义目标函数值记录器
                var_trace = np.zeros((iter_num, Lind))  # 染色体记录器，记录历代最优个体的染色体

                # 开始进化
                while True:
                    # 更新父代
                    parChrom = population
                    # 计算适应度矩阵
                    parFitnV = calFitnv_fix(parChrom, n_station)

                    # 记录当前种群信息
                    best_ind = np.argmax(parFitnV)  # 计算当代最优个体的序号
                    obj_trace[gen, 0] = np.sum(parFitnV) / parFitnV.shape[0]  # 记录当代种群的目标函数均值
                    obj_trace[gen, 1] = parFitnV[best_ind]  # 记录当代种群最优个体目标函数值
                    opt_line_balance = obj_trace[gen, 1]
                    var_trace[gen, :] = parChrom[best_ind, :]  # 记录当代种群最优个体的染色体
                    # 从父代选择进入交叉变异的个体
                    # 使用'sus'随机选择算子，片取Chrom得到所选择个体的染色体
                    SelCh = sus_selecting(parChrom, pop_size * gap, random.random())

                    # 交叉与变异
                    offChrom = Mutation_fix(Recombi_fix(SelCh, re_rate, random.random()), mu_rate, random.random())
                    # 选出父代中的精英个体
                    best_parChrom = dup_selecting(parChrom, parFitnV, pop_size * (1 - gap))

                    # 父代精英个体与子代合并，形成新的种群
                    population = np.append(best_parChrom, offChrom, axis=0)

                    # 停止机制
                    # 1.当连续60代不发生进化
                    stop_gen = 50
                    if gen > stop_gen:
                        if obj_trace[gen, 1] == obj_trace[gen - stop_gen, 1] and opt_linebalance1 > 0.1:
                            # print("连续不进化50代，停止")
                            break
                        if obj_trace[gen, 1] == obj_trace[gen - stop_gen * 2, 1] and opt_linebalance1 < 0.1:
                            # print("连续不进化100代，停止")
                            break
                    # 2.进化代数到达设定值
                    gen = gen + 1
                    if gen == iter_num:
                        break

                # 结果输出
                best_gen = np.argmax(obj_trace[:, [1]])
                opt_gene = list(map(int, var_trace[[best_gen], :][0].tolist()))
                opt_linebalance, res2, res3, res4, res5 = calFitness_fix(opt_gene, True, n_station)  # 解码得到表现型
                # print('Phase II Finish')
                stage_3 = 0
                threshold = 0.02
                if opt_linebalance > 0.1 or opt_linebalance1 > 0.1:
                    # 判断使用哪一阶段结果
                    if opt_linebalance1 > 0.1 and ((opt_linebalance1 - opt_linebalance) > threshold or res5 >= res15):
                        if round(res12, 1) != round(max(res13), 1) and constrains:
                            # print("Phase I result not match, enter phase III")
                            stage_3 = 1
                        else:
                            # print("Phase I Finish")
                            # res13,res12,res14=adjust_limit_station(res13, res12,res14)
                            job_tacttime.append(res12)
                            job_station_time.append(res13)
                            job_station_op.append(res14)
                    else:
                        # print("Phase II Finish")
                        res3, res2, res4 = adjust_limit_station(res3, res2, res4)
                        job_tacttime.append(res2)
                        job_station_time.append(res3)
                        job_station_op.append(res4)

                # 单独使用切分或者排序都无法满足固定工位时，使用双染色体GA

                if opt_linebalance1 <= 0.1 and opt_linebalance <= 0.1:
                    task_log = task_log + ' ' + 'Task' + str(ID) + ', 线体' + str(j) + ': 直接求解无法满足当前固定工位的线平衡计算，采用分段计算'
                    stage_3 = 1
                    # print("Phase II, enter phase III")

            stage_ori_constrains = constrains
            if stage_3 == 1:
                # print("enter phase III")
                calculated_sequence = []
                for stage_3_i in range(len(stage_3_inputs)):
                    constrains = stage_ori_constrains

                    # 处理第一工位固定过多
                    con_n_station1 = 0
                    for con in constrains:
                        if con[1] == 1:
                            con_n_station1 += 1
                    if con_n_station1 > 1:
                        constrains = constrains[con_n_station1 - 1:]

                    temp_stage_3_input = stage_3_inputs[stage_3_i]
                    calculated_flag = False
                    for j in calculated_sequence:
                        if temp_stage_3_input.tolist() == j:
                            calculated_flag = True
                            break
                    if calculated_flag == True and (stage_3_i != len(stage_3_inputs) - 1):
                        continue
                    else:
                        stage_3_input = stage_3_inputs[stage_3_i]
                        # print("新可行解探寻", stage_3_i + 1)
                    calculated_sequence.append(temp_stage_3_input.tolist())

                    # 生成一个可行顺序
                    # 切分成多个工段
                    # 对每个工段进行GA排序
                    stage_3_optime = []
                    for i in stage_3_input:
                        stage_3_optime.append(ge2ph[i])

                    # 切分成多个工段
                    c_p = []
                    # 打包相同固定工位的工序
                    package_dict = {}
                    temp_station = 9991
                    for i in range(len(constrains)):
                        # c_p.append(constrains[i][1])
                        if temp_station != constrains[i][1]:
                            package_lb = constrains[i][0]
                            package_hb = constrains[i][0]
                            temp_station = constrains[i][1]
                        else:
                            package_hb = constrains[i][0]
                        package_dict[constrains[i][1]] = [package_lb, package_hb]
                    # print("打包工段字典",package_dict)
                    new_constrains = []
                    num_removed_stations = 0
                    temp_list = stage_3_input.copy()
                    for key, values in package_dict.items():
                        temp_1 = temp_list.tolist().index(values[0])
                        temp_2 = temp_list.tolist().index(values[1]) + 1
                        stage_3_input[temp_1:temp_2] = values[0]
                        # 把替换的位置记录下来，算完要插回去
                        stage_3_optime[temp_1] = sum(stage_3_optime[temp_1:temp_2])
                        new_constrains.append([temp_1 + 1 - num_removed_stations, key])
                        num_removed_stations += temp_2 - temp_1 - 1
                    constrains = new_constrains

                    temp = 9999
                    del_list = []
                    for i in range(len(stage_3_input)):
                        if stage_3_input[i] == temp:
                            del_list.append(i)
                        temp = stage_3_input[i]

                    stage_3_input = np.delete(stage_3_input, del_list)
                    stage_3_optime_packed = [stage_3_optime[i] for i in range(0, len(stage_3_optime), 1) if
                                             i not in del_list]

                    # 做一个打包前后工序的转换字典
                    package_op_dict = {}
                    conv_input = list(stage_3_input)
                    stage_3_input = np.array(range(1, len(stage_3_input) + 1))

                    for i in stage_3_input:
                        package_op_dict[i] = conv_input[i - 1]

                    # 打包工段字典 {1: [18, 18], 4: [48, 65], 8: [90, 110]}
                    for key, values in package_dict.items():
                        for k, v in package_op_dict.items():
                            if v == values[0]:
                                package_op_dict[k] = list(range(values[0], values[1] + 1))

                    for i in range(len(constrains)):
                        c_p.append(constrains[i][1])

                    c_p_set = list(set(c_p))
                    c_p_set.sort(reverse=False)
                    k = [0]
                    kmax = [0]
                    for i in c_p_set:
                        temp = []
                        for j in constrains:
                            if j[1] == i:
                                temp.append(j[0])
                        k.append(min(temp) - 1)
                        kmax.append(max(temp))
                    k.append(max(stage_3_input) - 1)
                    kmax.append(max(stage_3_input))

                    slice_weights = []
                    raw_slices_n_station = []
                    for i in range(len(k) - 1):
                        lb = k[i]
                        ub = k[i + 1]

                        if i == 0:
                            diff_station = c_p_set[0] - 1
                        elif i == len(k) - 2:
                            ub = k[i + 1] + 1
                            diff_station = n_station - c_p_set[-1] + 1
                        else:
                            diff_station = c_p_set[i] - c_p_set[i - 1]
                        raw_slices_n_station.append(diff_station)
                        slice_weights.append(sum(stage_3_optime_packed[lb:ub]) / (diff_station + 0.01))

                    task_slices_op = []
                    task_slices_optime = []
                    task_slices_n_station = []
                    task_slices = []
                    # 重新切分工段
                    for i in range(len(slice_weights) - 1):
                        if slice_weights[i] > slice_weights[i + 1] and (raw_slices_n_station[i + 1] - 1) != 0:
                            task_slices.append(kmax[i + 1])
                            raw_slices_n_station[i] += 1
                            raw_slices_n_station[i + 1] -= 1
                        else:
                            task_slices.append(k[i + 1])

                    task_slices_n_station = raw_slices_n_station
                    for i, val in enumerate(raw_slices_n_station):
                        if val == 0:
                            del task_slices_n_station[i]
                            try:
                                del task_slices[i]
                            except:
                                del task_slices[i - 1]

                    for i in range(len(task_slices) + 1):
                        if i == 0:
                            task_slices_op.append(stage_3_input[0:task_slices[i]].tolist())
                            task_slices_optime.append(stage_3_optime_packed[0:task_slices[i]])
                        elif i == len(task_slices):
                            task_slices_op.append(stage_3_input[task_slices[-1]:].tolist())
                            task_slices_optime.append(stage_3_optime_packed[task_slices[-1]:])
                        else:
                            task_slices_op.append(stage_3_input[task_slices[i - 1]:task_slices[i]].tolist())
                            task_slices_optime.append(stage_3_optime_packed[task_slices[i - 1]:task_slices[i]])

                    # 提前判断是否会有空工位
                    empty_station_flag = False
                    for i in range(len(task_slices_op)):
                        proc_list = task_slices_op[i]  # 工序列表
                        # 判断是否有空工位
                        if len(proc_list) < task_slices_n_station[i]:
                            empty_station_flag = True
                            break
                    if empty_station_flag == True and (stage_3_i != len(stage_3_inputs) - 1):
                        # print("该可行解会产生空工位，跳过")
                        continue

                    slice_result = []
                    for i in range(len(task_slices_op)):
                        chrom_size = len(task_slices_op[i]) - 1  # 染色体长度
                        proc_time = task_slices_optime[i]  # 每个工序时间
                        proc_list = task_slices_op[i]  # 工序列表

                        # 判断是否有空工位
                        if len(proc_list) < task_slices_n_station[i]:
                            for j in range(task_slices_n_station[i]):
                                if j < len(proc_list):
                                    beta = package_op_dict[proc_list[j]]
                                    if type(beta) is list:
                                        slice_result.append(
                                            (1, proc_time[j], [proc_time[j]], [[op_dict[beta[0] - 1]]], 1))
                                    else:
                                        slice_result.append((1, proc_time[j], [proc_time[j]], [[op_dict[beta - 1]]], 1))
                                else:
                                    slice_result.append((1, 0, [0], [[]], 1))
                            continue

                        # line_balance, max_t, tack_time, station_op, line_var  # 产线节拍max_t,工站时间tack_time,工站工序station_op
                        # 如果只有一个工位，直接输出
                        if task_slices_n_station[i] == 1:
                            temp_time = sum(proc_time)
                            temp = []
                            for j in proc_list:  # 逆向转化
                                if isinstance(package_op_dict[j], list):
                                    temp += package_op_dict[j]
                                else:
                                    temp.append(package_op_dict[j])
                            temp_real_list = []
                            for k in temp:
                                temp_real_list.append(op_dict[k - 1])
                            slice_result.append((1, temp_time, [temp_time], [temp_real_list], 1))
                            continue

                        gap = 0.8
                        pop_size = 60  # 种群大小
                        re_rate = 0.6  # 交叉概率
                        mu_rate = 0.4  # 变异概率
                        population = initPop_fix_stage3(task_slices_n_station[i])
                        iter_num = 300

                        gen = 0  # 进化代数
                        opt_line_balance = 0  # 当前代数最优线平衡率
                        Lind = int(chrom_size)  # 计算染色体长度
                        obj_trace = np.zeros((iter_num, 2))  # 定义目标函数值记录器
                        var_trace = np.zeros((iter_num, Lind))  # 染色体记录器，记录历代最优个体的染色体

                        # 开始进化
                        while True:
                            # 更新父代
                            parChrom = population
                            # 计算适应度矩阵
                            parFitnV = calFitnv_fix_stage3(parChrom, task_slices_n_station[i])

                            # 记录当前种群信息
                            best_ind = np.argmax(parFitnV)  # 计算当代最优个体的序号
                            obj_trace[gen, 0] = np.sum(parFitnV) / parFitnV.shape[0]  # 记录当代种群的目标函数均值
                            obj_trace[gen, 1] = parFitnV[best_ind]  # 记录当代种群最优个体目标函数值
                            opt_line_balance = obj_trace[gen, 1]
                            var_trace[gen, :] = parChrom[best_ind, :]  # 记录当代种群最优个体的染色体
                            # 从父代选择进入交叉变异的个体
                            # 使用'sus'随机选择算子，片取Chrom得到所选择个体的染色体
                            SelCh = sus_selecting(parChrom, pop_size * gap, random.random())
                            # 交叉与变异
                            offChrom = Mutation_fix(Recombi_fix(SelCh, re_rate, random.random()), mu_rate,
                                                    random.random())
                            best_parChrom = dup_selecting(parChrom, parFitnV, pop_size * (1 - gap))
                            population = np.append(best_parChrom, offChrom, axis=0)

                            # 停止机制
                            # 1.当连续50代不发生进化
                            stop_gen = 50
                            if gen > stop_gen:
                                if obj_trace[gen, 1] == obj_trace[gen - stop_gen, 1]:
                                    break
                            # 2.进化代数到达设定值
                            gen = gen + 1
                            if gen == iter_num:
                                break

                        # 结果输出

                        best_gen = np.argmax(obj_trace[:, [1]])
                        opt_gene = list(map(int, var_trace[[best_gen], :][0].tolist()))
                        slice_result.append(calFitness_fix_stage3(opt_gene, True, task_slices_n_station[i]))  # 解码得到表现型
                    # line_balance, max_t, tack_time, station_op, line_var  # 产线节拍max_t,工站时间tack_time,工站工序station_op

                    # print('Phase III Finish')

                    stage_3_max_T = 0
                    stage_3_station_time = []
                    stage_3_station_op = []
                    for i in slice_result:
                        if i[1] > stage_3_max_T:
                            stage_3_max_T = i[1]
                        stage_3_station_time += i[2]
                        stage_3_station_op += i[3]

                    stage_3_station_time, stage_3_max_T, stage_3_station_op = adjust_limit_station(stage_3_station_time,
                                                                                                   stage_3_max_T,
                                                                                                   stage_3_station_op)

                    # 检查固定工位是否正确,目前仅支持前后均出现浮动空工位情况
                    for const in constrains_for_check:
                        if op_dict[const[0] - 1] in stage_3_station_op[const[1] - 1]:  # 此时固定正确
                            continue
                        else:
                            # 需要找到浮动到哪个错误位置, 洗掉错误位置
                            for station, station_op in enumerate(stage_3_station_op):
                                if op_dict[const[0] - 1] in station_op and len(station_op) == 1:
                                    stage_3_station_op[station] = []
                                    temp_time = stage_3_station_time[station]
                                    stage_3_station_time[station] = 0

                                if op_dict[const[0] - 1] in station_op and len(station_op) != 1:
                                    stage_3_station_op[station].remove(op_dict[const[0] - 1])
                                    stage_3_station_time[station] = stage_3_station_time[station] - op_time_dict[
                                        op_dict[const[0] - 1]]

                            stage_3_station_op[const[1] - 1].append(op_dict[const[0] - 1])
                            stage_3_station_time[const[1] - 1] = 0
                            for op in stage_3_station_op[const[1] - 1]:
                                stage_3_station_time[const[1] - 1] += op_time_dict[op]

                    # 切分导致极端固定工位情况下，空闲时间分配不均现象，用标准差寻最优解
                    # 方法：遍历所有有固定工序的工站，把其前一个工站的最末尾的工序逐一后移，看标准差变化
                    checked_station = []
                    for const in constrains_for_check:
                        if const[1] not in checked_station:
                            while True:
                                remove_flag = False  # 可以从末尾移动多个
                                current_sd = np.std(stage_3_station_time, ddof=1)
                                temp_stage_3_station_time = stage_3_station_time.copy()
                                if const[1] > 1 and len(stage_3_station_op[const[1] - 2]) > 1:
                                    removed_op = stage_3_station_op[const[1] - 2][-1]  # 前一个工站最后一个工序的编号
                                    if removed_op not in constrained_op:
                                        temp_stage_3_station_time[const[1] - 2] = temp_stage_3_station_time[
                                                                                      const[1] - 2] - op_time_dict[
                                                                                      removed_op]
                                        temp_stage_3_station_time[const[1] - 1] = temp_stage_3_station_time[
                                                                                      const[1] - 1] + op_time_dict[
                                                                                      removed_op]
                                        new_sd = np.std(temp_stage_3_station_time, ddof=1)
                                        if new_sd < current_sd:
                                            stage_3_station_time = temp_stage_3_station_time.copy()
                                            stage_3_station_op[const[1] - 1].insert(0, stage_3_station_op[
                                                const[1] - 2].pop(-1))
                                            remove_flag = True
                                if remove_flag == False:
                                    break
                            checked_station.append(const[1])  # 记录已经完成校验的工位

                    job_tacttime.append(stage_3_max_T)
                    job_station_time.append(stage_3_station_time)
                    job_station_op.append(stage_3_station_op)

                    if empty_station_flag == False:
                        break

        # 固定节拍
        if isFixStation == 1:

            # 读取理想CT,最大最小工位数
            expected_CT = df['EXPECTEDTACTTIME'].values[0]
            min_n_station = 1  # 默认值
            max_n_station = 100
            if df['MINSTATIONNUM'].values[0]:
                min_n_station = df['MINSTATIONNUM'].values[0].astype(int)
            if df['MAXSTATIONNUM'].values[0]:
                max_n_station = df['MAXSTATIONNUM'].values[0].astype(int)
            df.drop(['MINSTATIONNUM', 'MAXSTATIONNUM', 'EXPECTEDTACTTIME'], axis=1, inplace=True)

            # 读取总工时、瓶颈工序工时
            T = df['OPTIME'].sum()  # 总工时
            max_T = df['OPTIME'].max()  # 瓶颈工序，在不限工站数的情况下，即为理论最小CT
            # 如果瓶颈工序时间大于理想节拍，则输入数据判定为有误：
            if max_T > expected_CT:
                job_status.append(2)
                task_log = task_log + ' ' + '瓶颈工序时间大于理想最大节拍，请增大理想节拍，使之大于' + str(max_T) + 's'
                continue

            # 炸开PREOP列
            df = pd.concat([df, df['PREOP'].str.split(',', expand=True)], axis=1)  # 炸开PREOP列
            df.drop(['PREOP'], axis=1, inplace=True)
            # 用自然数重新编号
            for index, row in df.iterrows():
                df = df.replace(row['OPNUM'], index + 1)
            # 删除A线与B线之间跨线的约束
            df.iloc[:, 3:] = df.iloc[:, 3:].applymap(lambda x: x if x in df['OPNUM'].tolist() else None)
            # 读取约束 定义固定工站[工序，工位]
            constrains = df[['OPNUM', 'FIXSTATION']][df['FIXSTATION'].notnull()].astype(int).values.tolist()
            df.drop(['FIXSTATION'], axis=1, inplace=True)

            # 设定GA参数
            gap = 0.8  # 代沟，gap%的父代被选作母体用于交叉变异
            pop_size = 20  # 种群大小
            re_rate = 0.7  # 交叉概率
            mu_rate = 0.3  # 变异概率
            iter_num = 150  # 迭代次数

            # 初始化AOV图（种群初始化和变异中要用）
            temp = df.fillna(0).drop(['OPTIME'], axis=1).astype(int).set_index('OPNUM').T.to_dict('list')
            G = {}
            for key, value in temp.items():
                G[key] = [i for i in value if i != 0]

            # 生成基因型表现型转换矩阵
            ge2ph = dict(zip(df['OPNUM'].tolist(), df['OPTIME'].tolist()))
            chrom_size = len(df)  # 染色体长度

            # 初始化种群
            try:
                population, circle_error = initPop()  # 输出初始化种群基因型矩阵
            except ValueError:
                task_log = task_log + ' ' + 'Task' + str(ID) + ', 线体' + str(j) + ':原始数据有误'
                job_status.append(2)
                continue
            if circle_error == True:
                task_log = task_log + ' ' + 'Task' + str(ID) + ', 线体' + str(j) + ':内有矛盾工序优先关系'
                job_status.append(2)
                continue

            # 计算最大最小工站区间
            phen = []
            k = 0
            for j in population[1]:
                phen.append(ge2ph[j])
                k = k + 1
            max_n = cal_n_station(phen, max_T)
            min_n = cal_n_station(phen, expected_CT)
            max_n = max_n_station if max_n > max_n_station else max_n
            min_n = min_n_station if min_n < min_n_station else min_n

            # 遍历所有可行n_station,初始化每个n_station结果记录器
            df.set_index(["OPNUM"], inplace=True)
            opt_fitness_list = []
            opt_stationtime_list = []
            opt_station_op_list = []
            n_list = range(min_n, max_n + 1)  # 所有可行n_station

            # 开始遍历
            for n in n_list:
                n_station = n

                # 初始化记录器
                gen = 0  # 进化代数
                opt_line_balance = 0  # 当前代数最优线平衡率
                Lind = int(chrom_size)  # 计算染色体长度
                obj_trace = np.zeros((iter_num, 2))  # 定义目标函数值记录器
                var_trace = np.zeros((iter_num, Lind))  # 染色体记录器，记录历代最优个体的染色体

                # 开始进化
                while opt_line_balance < 0.94:
                    # 更新父代
                    parChrom = population
                    # 计算适应度矩阵
                    parFitnV = calFitnv(parChrom)
                    # 记录当前种群信息
                    best_ind = np.argmax(parFitnV)  # 计算当代最优个体的序号
                    obj_trace[gen, 0] = np.sum(parFitnV) / parFitnV.shape[0]  # 记录当代种群的目标函数均值
                    obj_trace[gen, 1] = parFitnV[best_ind]  # 记录当代种群最优个体目标函数值
                    opt_line_balance = obj_trace[gen, 1]
                    var_trace[gen, :] = parChrom[best_ind, :]  # 记录当代种群最优个体的染色体
                    # 从父代选择进入交叉变异的个体
                    # 使用'sus'随机选择算子，同时片取Chrom得到所选择个体的染色体
                    SelCh = sus_selecting(parChrom, pop_size * gap, random.random())

                    # 交叉与变异
                    offChrom = Mutation(Recombi(SelCh, re_rate, random.random()), mu_rate, random.random())
                    # 选出父代中的精英个体
                    best_parChrom = dup_selecting(parChrom, parFitnV, pop_size * (1 - gap))
                    # 父代精英个体与子代合并，形成新的种群
                    population = np.append(best_parChrom, offChrom, axis=0)

                    # 停止机制
                    # 当连续30代不发生进化
                    if gen > 30:
                        if obj_trace[gen, 1] == obj_trace[gen - 30, 1]:
                            break
                    # 进化代数到达设定值
                    gen = gen + 1
                    if gen == iter_num:
                        break
                # 输出当前工站数结果
                best_gen = np.argmax(obj_trace[:, [1]])
                opt_gene = list(map(int, var_trace[[best_gen], :][0].tolist()))
                temp1 = Ge2Ph(var_trace[[best_gen], :])
                opt_phen = temp1[0]
                opt_linebalance, res2, res3, res4, res5 = calFitness(n_station, T, opt_phen, opt_gene, True)  # 解码得到表现型
                opt_fitness_list.append(opt_linebalance)
                opt_stationtime_list.append(res3)
                opt_station_op_list.append(res4)

            # 找到最优工站数
            final_opt = max(opt_fitness_list)

            if final_opt < 0.1:
                task_log = task_log + ' 无法满足固定工位'
                job_status.append(2)
                continue

            # 输出最优工站数的结果
            job_linebalance.append(final_opt)
            temp_sta = n_list[opt_fitness_list.index(max(opt_fitness_list))]
            job_stationnum.append(temp_sta)  # 最优工站数
            job_station_time.append(opt_stationtime_list[opt_fitness_list.index(max(opt_fitness_list))])
            res = round(T / (temp_sta * final_opt), 2)  # 最优工站数时的节拍
            job_tacttime.append(res)
            job_station_op.append(opt_station_op_list[opt_fitness_list.index(max(opt_fitness_list))])

        job_status.append(3)  # job任务成功执行

    # 任务结果打印
    # print('job_status', job_status)
    # print('是否固定工位',isFixStation)
    if 2 in job_status:
        task_status = 2
    else:
        task_status = 3

    # print('task_log', task_log)
    # print('task_status', task_status)

    sqls = []
    connection = pymysql.connect(host=db_url, user=user, passwd=password, port=port, db=database, charset='utf8')
    cursor = connection.cursor()  # cursor数据库交互对象
    try:
        if task_status == 3:
             print('job_linebalance', job_linebalance)
        for job_id in range(len(Line_list)):
            if len(empty_stations) >=1 and empty_stations[0]!=9999:
                #如果有人工空工位，再插入回原位
                for empty_station in empty_stations:
                    job_station_time[job_id].insert(empty_station-1, 0)
                    job_station_op[job_id].insert(empty_station-1, [])
                    job_stationnum[job_id]=len(job_station_time[job_id])
            # print('job_tacttime', job_tacttime)
            # print('job_stationnum', job_stationnum)
            LineTactTime = max(job_tacttime)
            UPH = 3600 / LineTactTime
            LineBalanceRate = data['OPTIME'].sum() / (sum(job_stationnum) * LineTactTime)
            # print('总节拍', LineTactTime, 'UPH', UPH, '总线平衡', LineBalanceRate)
            # print('工站时间', job_station_time)
            # print('工站工序', job_station_op)

            # ga_task
            sqls.append("UPDATE ga_task SET TASKSTATUS=" + str(task_status) + ",TASKLOG='" + task_log + "',UPH=" + str(
                UPH) + ",LINEBALANCERATE=" + str(LineBalanceRate) + ",LINETACTTIME=" + str(
                LineTactTime) + " WHERE ID='" + str(ID) + "'")
            # ga_assemline
            for i in range(len(Line_list)):
                sqls.append("UPDATE ga_assemline SET STATIONNUM=" + str(job_stationnum[i]) + ",TACTTIME=" + str(
                    job_tacttime[i]) + " WHERE ID='" + str(Line_list[i]) + "'")
            # ga_processstation
            # GA_stationOperation
            for i in range(len(Line_list)):
                for j in range(len(job_station_time[i])):
                    ps_sql = "INSERT INTO ga_processstation (LINEID, STATIONNO, STATIONTIME) VALUES ('" + str(
                        Line_list[i]) + "'," + str(j + 1) + "," + str(job_station_time[i][j]) + ")"
                    cursor.execute(ps_sql)
                    ps_id = cursor.lastrowid
                    connection.commit()
                    for k in range(len(job_station_op[i][j])):
                        sqls.append(
                            "INSERT INTO ga_stationoperation (STATIONID, OPID, PROCTIME) VALUES (" + str(
                                ps_id) + ",'" + str(
                                job_station_op[i][j][k]) + "'," + str(op_time_dict[job_station_op[i][j][k]]) + ")")
        else:
            sqls.append(
                "UPDATE ga_task SET TASKSTATUS=" + str(task_status) + ",TASKLOG='" + task_log + "' where ID='" + str(ID) + "'")
        for sql in sqls:
            cursor.execute(sql)
        connection.commit()
    finally:
        cursor.close()
        connection.close()

# print('GAComplete')
# print("End: ", time.strftime("%Y-%m-%d %H:%M:%S", time.localtime()))
