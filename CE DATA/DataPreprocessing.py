import pandas as pd


# Read the csv dataset file from PC
dataset = pd.read_csv('F:/data preprocessing/dataset.csv')

# Drop the rows that have all null values in each input column
df = dataset.dropna(how='all',  subset=['MacAddr1', 'MacAddr2', 'MacAddr3','MacAddr4','MacAddr5','MacAddr6','MacAddr7','MacAddr8','MacAddr9','MacAddr10','MacAddr11','MacAddr12','MacAddr13','MacAddr14', 'MacAddr15','MacAddr16','MacAddr17','MacAddr18'
                                        ,'MacAddr19','MacAddr20','MacAddr21','MacAddr22','MacAddr23','MacAddr24'])
# Replace empty values with -100
df.fillna(-100, inplace = True)
df.to_csv ('F:/data preprocessing/dataset1.csv', index = False, header=True)