/**
 * 工作类型
 */
type WorkType = {
    name: string;
    description: string;
    duration: number;
    tags: string[];
    planTime?: Date;
    createTime: Date;
    updateTime: Date;
}

export default WorkType;